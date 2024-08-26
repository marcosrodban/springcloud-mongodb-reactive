


var eventSource = new EventSource('');
var conexiones = [];


function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}


function connect() {
	eventSource = new EventSource('http://localhost:8899/mongodb/stream-zip');

	eventSource.onopen = function() {
		setConnected(true);
		console.log('Conexion establecia: ');
	};



	eventSource.onmessage = (event) => {
		showGreeting(event.data);
		console.log('message data:', event.data);
	}

	eventSource.addEventListener('my-event', (event) => {
		console.log('event data:', event.data);
	});

}


function connectstartDatos1(url, funcUpdateUI, numeroconexion, funcStartStopUI) {
	if (conexiones[numeroconexion] == null || conexiones[numeroconexion] == undefined) {
		conexiones[numeroconexion] = createConnection(url, funcUpdateUI, funcStartStopUI);
	} else if (conexiones[numeroconexion].readyState == 1) {
		conexiones[numeroconexion].close();
		funcStartStopUI(false);
	} else if (conexiones[numeroconexion].readyState == 2) {
		conexiones[numeroconexion] = createConnection(url, funcUpdateUI, funcStartStopUI);
	}
}

function createConnection(url, funcUpdateUI, funcStartStopUI) {
	//EventSource.CONNECTING = 0; // conectando o reconectando
	//EventSource.OPEN = 1;       // conectado
	//EventSource.CLOSED = 2; 

	var eventSourceDatosObjTemp = new EventSource(url);

	eventSourceDatosObjTemp.onopen = function() {
		console.log('Conexion establecia: ');
		funcStartStopUI(true);
	};
	eventSourceDatosObjTemp.addEventListener('Close', (event) => {
		funcStartStopUI(false);
		console.log('connection closed:');
	});
	eventSourceDatosObjTemp.addEventListener('periodic-event', (event) => {
		funcUpdateUI(event);
		console.log('event data:', event.data);
	});
	return eventSourceDatosObjTemp;
}

function disconnect() {
	eventSource.close();
	setConnected(false);
	console.log("Disconnected");
}

function showGreeting(message) {
	if ($('#conversation tbody tr').length > 10) {
		$('#conversation tbody tr').remove();

	}
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function() {
	$("form").on('submit', (e) => e.preventDefault());
	$("#connect").click(() => connect());
	$("#disconnect").click(() => disconnect());
	$("#startDatos1").click(() => connectstartDatos1('http://localhost:8899/mongodb/stream-zip',
		(e) => { $("#eldato1").text(e.data); }, 0,
		(e) => { $("#startDatos1").text(e ? "stop 1" : "start 1"); }
	));
	$("#startDatos2").click(() => connectstartDatos1('http://localhost:8899/mongodb/stream-merge',
		(e) => { $("#eldato2").text(e.data); }, 1,
		(e) => { $("#startDatos2").text(e ? "stop 2" : "start 2"); }
	));




});


