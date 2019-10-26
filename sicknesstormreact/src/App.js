import React, { Component } from 'react';
import './App.css';
import Formulaire from './formulaire';
import BodyMap from './BodyMap';
import AttackPhase from './AttackPhase';
import MoovFortify from './MoovFortify';
import PlacePawnInterface from './PlacePawnInterface';
class App extends Component {
  constructor(props){
    super(props)
    this.handleEvent = this.handleEvent.bind(this);
  }
  state = {roundPhase:null}
  
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

  handleEvent(phase){
    this.setState({roundPhase: "ef"});
    this.componentDidMount();
    console.log("event sent");
  }

  render() {
    return (
      <div className="App">
        <BodyMap/>
        <Formulaire/>
        <AttackPhase/>
        <MoovFortify/>
        <PlacePawnInterface/>

        {this.state.roundPhase == "INITIALIZE" ? <Formulaire updatephase={this.handleEvent}/> : "" }
        {this.state.roundPhase != "INITIALIZE" ? <BodyMap/> : ""}
        {this.state.roundPhase != "INITIALIZE" ? <AttackPhase/> : ""}


      </div>
    );
  }
}

export default App;
