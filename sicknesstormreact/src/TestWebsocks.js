import React from 'react';
import SockJsClient from 'react-stomp';

export default class TestWebsocks extends React.Component{

      componentDidMount = async () =>{
      }    
      
      sendMessage = (msg) => {
        this.clientRef.sendMessage('/topic/user', msg);
      }

      testConsole = (msg) => {
        console.log("OK CONNECTED");
      }
     
      render() {
        return (
          <div>
            <SockJsClient url='http://localhost:8080/websocket-example' topics={['/topic/user']}
               onConnect={this.testConsole} onMessage={(msg) => { console.log(msg); }}  />
          </div>
        );
      }
}