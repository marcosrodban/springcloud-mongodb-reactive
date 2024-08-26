/**
 * Define funciones de ejecución inmediata independiente del DOM
 */

(function($) {

	//Se crea el objeto base, que alberga toda la gestión de los componentes SWS (Steria web scripts namespace), dentro de la jerarquía de JQuery
	$.mws = $.mws || {};
	$.extend($.mws, {
		url : '',
		ws : '',
		customOnMessage: function (){},
		customOnClose: null,
		openconnection: function(url , onmessage) {
			$.mws.url = url;
			$.mws.ws = new WebSocket(url);
			$.mws.customOnMessage = onmessage;
			$.mws.ws.onmessage = function(data){
				console.log("nuevo mensaje:"+data.data);
				$.mws.customOnMessage(data.data);
			}
			$.mws.connectionstate();
		},
		sendmessage: function(message) {
			$.mws.ws.send(message);
		},
		closeconnection: function() {
			console.log("Disconnecting");
			if ($.mws.ws != null) {
				$.mws.ws.close();
			}
			$.mws.connectionstate();
		},
		connectionstate: function(){
			if ($.mws.ws != null) {
				console.log("estado de la conexion:"+ $.mws.ws.readyState);
				return $.mws.ws.readyState;
			}
			return;
		}
		
	
	});


})(jQuery);
