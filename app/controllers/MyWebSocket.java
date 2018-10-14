package controllers;


import play.Logger;
import play.mvc.Http;
import play.mvc.WebSocketController;

import static play.libs.F.Matcher.Equals;
import static play.mvc.Http.WebSocketEvent.SocketClosed;
import static play.mvc.Http.WebSocketEvent.TextFrame;

public class MyWebSocket extends WebSocketController {

    public static void echo() {
        while(inbound.isOpen()) {
            Http.WebSocketEvent e = await(inbound.nextEvent());

            for(String quit: TextFrame.and(Equals("quit")).match(e)) {
                outbound.send("Bye!");
                disconnect();
            }

            for(String message: TextFrame.match(e)) {
                outbound.send(message);
            }

            for(Http.WebSocketClose closed: SocketClosed.match(e)) {
                Logger.info("Socket closed!");
            }
        }
    }
}
