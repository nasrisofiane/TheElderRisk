import React, { Component } from 'react';
import './App.css';
import Formulaire from './formulaire';
import BodyMap from './BodyMap';
import AllTerritories from './AllTerritories';
class App extends Component {
  render() {
    return (
      <div className="App">
        <BodyMap/>
        <Formulaire/>
        <AllTerritories name="attackers"/>
        <AllTerritories name="defenders"/>
      </div>
    );
  }
}

export default App;
