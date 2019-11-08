import React, { Component } from 'react';
import './App.css';
import Formulaire from './formulaire';
import BodyMap from './BodyMap';
import AttackPhase from './AttackPhase';
import MoovFortify from './MoovFortify';
import PlacePawnInterface from './PlacePawnInterface';
import PlayerTurn from './PlayerTurn';
import Websocket from 'react-websocket';

class App extends Component {
  constructor(props){
    super(props)
    this.handleEvent = this.handleEvent.bind(this);
  }
  state = {roundPhase:null, players:null, count: 90}

  handleData(data) {
    let result = JSON.parse(data);
    this.setState({count: this.state.count + result.movement});
  }
  
  async componentDidMount(){
    try{
        let response = await fetch(`http://localhost:8080/roundphase`);
        if(response.ok){
            let data = await response.json()
            this.setState({
              roundPhase : data
            })
            await this.getAllPlayers();
            console.log(data);
            return(
              <Websocket url='ws://http://localhost:8080/websocket-example'
              onMessage={this.handleData.bind(this)}/>
            );
            throw new Error(response.statusText);
        }
            
    }
    catch(err){
    }
  }

  async getAllPlayers() {
    try {
        let result = await fetch('http://localhost:8080/players');
        let data = await result.json()
        this.setState({players:data});
        console.log(data)
    } catch(e){
    }
} 



  handleEvent(phase){
    this.componentDidMount();
    console.log("event sent");
  }

  render() {
    return (
      
      <div className="App">
        <strong>Count: {this.state.count}</strong>
        
        {this.state.roundPhase == "INITIALIZE" ? <Formulaire updatephase={this.handleEvent} /> : ""}
        {this.state.roundPhase != "INITIALIZE" && this.state.players != null ? <BodyMap players={this.state.players} /> : "LOADING..."}
      </div>
    );
  }
}

export default App;
