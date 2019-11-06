import React from 'react';
import AttackPhase from './AttackPhase';
import MoovFortify from './MoovFortify';
import PlayerTurn from './PlayerTurn';
import PlacePawnInterface from './PlacePawnInterface';
import SvgBody from './SvgBody';

export default class BodyMap extends React.Component{
    
    constructor(){
            super();
            this.state = {isLoaded : false ,territories:{}, roundPhase:null, playerTurn : null, prevTerritorySelectedAttacker:null, prevTerritorySelectedDefender:null, territoryAttackerSelected:null, territoryDefenderSelected:null, switchSelect:false};
            this.handleEvent = this.handleEvent.bind(this);
        }
        
    async componentDidMount(){
        try{
            let response = await fetch(`http://localhost:8080/territories`);
            if(response.ok){
                let data = await response.json()
                let dataTreated = data.map((territories) => { return [territories.id, territories.name.toLowerCase(), territories.pawn, territories.player.name, territories.player.id]});
                this.setState({
                    isLoaded : true,
                    territories : dataTreated.sort(function(a, b) {return a[0] - b[0];})
                })
                await this.getPhase();
                await this.getPlayerTurn();
                throw new Error(response.statusText);
            }       
        }
        catch(err){
              console.log(err);
        }
    }

    cleanBodyFromSelectedTerritories = async () =>{
        this.state.territoryAttackerSelected[1].classList.remove("attacker");
        this.state.territoryDefenderSelected[1].classList.remove("defender");
    }

    territorySelected = async (territory) =>{
        let found = false;
        let territoryDom = territory;
        for(let i = 0; i < this.state.territories.length; i++){
            if(territory.id == this.state.territories[i][1]){
                found = true;
                
                territory = this.state.territories[i];
                break;
            }
        }
        if(found == true && (this.state.switchSelect == false && (this.state.roundPhase == "ATTACK" || this.state.roundPhase == "MOVEFORTIFY") ) || (found == true && this.state.roundPhase == "PLACEPAWN") ){
            if(this.state.prevTerritorySelectedAttacker != null){
                
                this.state.prevTerritorySelectedAttacker.classList.remove('attacker');
            }
            territoryDom.classList.add('attacker');
            await this.setState({prevTerritorySelectedAttacker:territoryDom, territoryAttackerSelected:[territory, territoryDom]});
            if(this.state.switchSelect == false && (this.state.roundPhase == "ATTACK" || this.state.roundPhase == "MOVEFORTIFY")){
                this.setState({switchSelect:true});
            }
        }
        else if(found == true && this.state.switchSelect == true && (this.state.roundPhase == "ATTACK" || this.state.roundPhase == "MOVEFORTIFY")){
            if(this.state.prevTerritorySelectedDefender != null){
                this.state.prevTerritorySelectedDefender.classList.remove('defender');
            }
            territoryDom.classList.add('defender');
            await this.setState({territoryDefenderSelected:[territory, territoryDom]});
            this.setState({switchSelect:false,  prevTerritorySelectedDefender:territoryDom});
        }
        else{
            console.log("error");
        }
        
    }

    async getPlayerTurn(){
        try{
            let response = await fetch(`http://localhost:8080/playerturn`);
            if(response.ok){
                let data = await response.json()
                this.setState({
                  playerTurn : data.name
                })
                console.log(data.name);
                throw new Error(response.statusText);
            }
        }
        catch(err){
        }
    }

    //retrieve the actual phase of the round, when it's up to date it set the state.
    async getPhase(){
        try{
            let response = await fetch(`http://localhost:8080/roundphase`);
            if(response.ok){
                let data = await response.json()
                await this.setState({
                  roundPhase : data
                })
                console.log(data);
                throw new Error(response.statusText);
            }
        }
            catch(err){
        }
    }

    //method created to be passed in a child to call the fetch that retrieve the phase data.
    handleEvent(){
        this.getPhase();
        this.componentDidMount();
        this.getPlayerTurn();
        console.log("event sent");
      }
        
    handleClick(e) {
        var attaque = e.target.id;
        e.preventDefault();
        console.log('continant selectionner est le '+attaque);
      }
    
    render(){
        return(     
            <div>
                    {this.state.isLoaded ? <SvgBody updateTerritories={this.state.territories} territorySelected={this.territorySelected}></SvgBody> : "Loading..."}
                    {this.state.roundPhase != "INITIALIZE" ? <PlayerTurn playerturn={this.state.playerTurn} players={this.props.players}/> : ""}
                    <div id="phase-interface">
                        {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "PLACEPAWN" ? <PlacePawnInterface updatephase={this.handleEvent} territoryAttackerSelected={this.state.territoryAttackerSelected}  /> : ""}
                        {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "ATTACK" ? <AttackPhase updatephase={this.handleEvent} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                        {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "MOVEFORTIFY" ? <MoovFortify updatephase={this.handleEvent} cleanSelected={this.cleanBodyFromSelectedTerritories} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                    </div>
            </div>
              
        );
    }
   
    
}