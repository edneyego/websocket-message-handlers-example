var BUFF_SIZE = 512;
var buffer = new Array(BUFF_SIZE);
var socket = 0;

function check() {
	// Check for the various File API support.
	if (window.File && window.FileReader && window.FileList && window.Blob) {
		var dropZone = document.getElementById('drop_zone');
		dropZone.addEventListener('dragover', handleDragOver, false);
		dropZone.addEventListener('drop', uploadFile, false);
	} else {
		alert('The File APIs are not fully supported in this browser.');
	}

	if ('WebSocket' in window) {
		var ws_url = '';
		if (window.location.protocol == "http:") {
			ws_url = 'ws://' + window.location.host + window.location.pathname
					+ 'fileUpload';
		} else if (window.location.protocol == "https:") {
			ws_url = 'wss://' + window.location.host + window.location.pathname
					+ 'fileUpload';
		} else {
			alert('cannot construct websocket url');
		}

		socket = new WebSocket(ws_url);
		socket.onopen = wsOpen;
		socket.onerror = wsError;
		socket.onmessage = wsMessage;
		socket.onclose = wsClose;

	} else {
		alert('This browser does not support websockets');
	}
}

function handleDragOver(evt) {
	evt.stopPropagation();
	evt.preventDefault();
	evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
}

function uploadFile(evt) {
	evt.stopPropagation();
	evt.preventDefault();

	var f = evt.dataTransfer.files[0];
	var chunks = Math.ceil(f.size / BUFF_SIZE);
	var msg = new Object();
	
	msg.op = 'put';
	msg.fname = f.name;
	msg.fsize = f.size;
	socket.send(JSON.stringify(msg));
	
	for ( var j = 0; j < chunks; j++) {
		var reader = new FileReader();

		reader.onload = sendBinary;
		var upper = BUFF_SIZE * (j + 1);
		if (upper > f.size) {
			upper = f.size;
			window.setTimeout(listFiles, 2000);
		}
		var blob = f.slice(BUFF_SIZE * j, upper);
		reader.readAsArrayBuffer(blob);
	}
}

function sendBinary(evt) {
	var data = evt.target.result.slice(0);
	console.log('send binary: ' + data.byteLength);
	socket.binaryType = "arraybuffer";
	socket.send(data);
	console.log(socket.bufferedAmount);
}

function listFiles() {
	var msg = new Object();
	msg.op = 'list';
	console.log('send text: ' + JSON.stringify(msg));
	socket.send(JSON.stringify(msg));
}

function wsOpen() {
	console.log('ws connection is established');
	listFiles();
}

function wsError(error) {
	conosle.error(error);
}

function wsMessage(evt) {
	console.log('received msg: ' + evt.data);
	var files = JSON.parse(evt.data);
	var table = document.getElementById('list');
	while (table.rows.length > 0)
		table.deleteRow(table.rows.length - 1);

	for ( var i = 0, f; f = files[i]; i++) {
		var row = table.insertRow(i);
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		cell1.innerHTML = f.name;
		cell2.innerHTML = f.size;
	}
	document.getElementById('list_zone').style.height = 'auto';
}

function wsClose(evt) {
	console.log('close: ' + evt);
}
