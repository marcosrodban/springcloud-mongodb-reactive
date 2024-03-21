


var eventSource = new EventSource('');
 



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
   eventSource = new EventSource('http://localhost:8898/mongodb/contactoses');

eventSource.onopen = function () {
     setConnected(true);
    console.log('Conexion establecia: ' + frame);
};
 


  eventSource.onmessage = (event) => {
	showGreeting(event.data);
    console.log('message data:', event.data);
  }

  eventSource.addEventListener('my-event', (event) => {
    console.log('event data:', event.data);
  });

}

function disconnect() {
    eventSource.close();
    setConnected(false);
    console.log("Disconnected");
}



function showGreeting(message) {
	if($('#conversation tbody tr').length > 10){
		$('#conversation tbody tr').remove();
		
	}
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $( "form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());



});


