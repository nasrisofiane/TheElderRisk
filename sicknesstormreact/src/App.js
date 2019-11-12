import React, { Component } from 'react';
import './App.css';
import Formulaire from './formulaire';
import BodyMap from './BodyMap';
import SockJsClient from 'react-stomp';

class App extends Component {
  constructor(props){
    super(props)
    this.handleEvent = this.handleEvent.bind(this);
  }
  state = {roundPhase:null, territories:{}, players:null, userInfos:null, receivedMessageChange: null, game:null}

  
  async componentDidMount(){
    try{
        let response = await fetch(`http://localhost:8080/roundphase`);
        if(response.ok){
            let data = await response.json()
            this.setState({
              roundPhase : data
            })
            console.log(data);
           
            throw new Error(response.statusText);
        }
            
    }
    catch(err){
    }
    
  }

  sendMessage = (msg) => {
    this.clientRef.sendMessage('/app/message', msg);
  }

  sendMessageInitiliazePhase = async () => {
    await this.clientRef.sendMessage('/app/initializegame', "msg");
  }

  sendMessageAddPlayer = async (msg) => {
    this.setState({userInfos: msg});
    this.clientRef.sendMessage('/app/getPlayerJoined', msg);
  }

  sendMessageCloseMoveFortifyPhase = async () => {
    this.clientRef.sendMessage('/app/closemovefortifystep', "msg");
  }

  sendMessageGetTerritories = async () => {
    this.clientRef.sendMessage('/app/territories', "msg");
  }

  sendMessageToAddPawns = async (territory, nbOfPawns) => {
    let AddPawnsInfos = [territory, nbOfPawns];
    await this.clientRef.sendMessage('/app/addpawn', JSON.stringify(AddPawnsInfos));
  }

  sendMessageToFight = async (territoryAttacker, territoryDefender, nbOfPawns) => {
    let fightInfos = [territoryAttacker, territoryDefender, nbOfPawns];
    await this.clientRef.sendMessage('/app/fight', JSON.stringify(fightInfos));
  }

  sendMessageToAnswerFight = async (nbdef) => {
    await this.clientRef.sendMessage('/app/answerfight', JSON.stringify(nbdef));
  }

  messageReceived = async (msg) => {
    if(typeof msg.playerList != 'undefined'){
       await this.setState({roundPhase:msg.phase ,territories:msg.territories, players:msg.playerList, game:msg});
       
    }
    else{
      this.setState({players:msg});
    }

    
    console.log(this.state.territories);
  }

  handleEvent(phase){
    this.componentDidMount();
    console.log("event sent");
  }

  render() {
    return (
      
      <div className="App">
        <SockJsClient url='http://localhost:8080/websocket-example' topics={['/topic/message']} onMessage={this.messageReceived}  ref={ (client) => { this.clientRef = client; }} />
        {this.state.roundPhase == "INITIALIZE" ? <Formulaire updatephase={this.handleEvent} sendMessage={this.sendMessageAddPlayer} sendMessageInitiliazePhase={this.sendMessageInitiliazePhase} players={this.state.players}/> : ""}
        {this.state.roundPhase != "INITIALIZE" && this.state.players != null ? <BodyMap sendMessageToAnswerFight={this.sendMessageToAnswerFight} sendMessageToFight={this.sendMessageToFight} sendMessageToAddPawns={this.sendMessageToAddPawns} territories={this.state.territories} sendMessageGetTerritories={ this.sendMessageGetTerritories} sendMessage={this.sendMessage} sendMessageCloseMoveFortifyPhase={this.sendMessageCloseMoveFortifyPhase} players={this.state.players} game={this.state.game} userName={this.state.userInfos}/> : "LOADING..."}
      </div>
    );
  }
}

export default App;
