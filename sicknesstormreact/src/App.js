import React, { Component } from 'react';
import './App.css';
import Formulaire from './formulaire';
import BodyMap from './BodyMap';
class App extends Component {
  render() {
    return (
      <div className="App">
        <BodyMap/>
        <Formulaire/>
      </div>
    );
  }
}

export default App;
