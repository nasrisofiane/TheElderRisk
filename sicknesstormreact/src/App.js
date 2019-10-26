import React, { Component } from 'react';
import './App.css';
import Formulaire from './formulaire';
import BodyMap from './BodyMap';
import AttackPhase from './AttackPhase';
import MoovFortify from './MoovFortify';
import PlacePawnInterface from './PlacePawnInterface';
class App extends Component {
  

  handleChildUnmount(){
    this.setState({renderChild: false});
}

  render() {
    

    return (
      <div className="App">
        <BodyMap/>
        <Formulaire/>
        <AttackPhase/>
        <MoovFortify/>
        <PlacePawnInterface/>

      </div>
    );
  }
}

export default App;
