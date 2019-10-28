import React from 'react';
import AttackPhase from './AttackPhase';
import MoovFortify from './MoovFortify';
import PlayerTurn from './PlayerTurn';
import PlacePawnInterface from './PlacePawnInterface';
export default class BodyMap extends React.Component{
    
    constructor(){
            super();
            this.state = {isLoaded : false ,territories:{}, roundPhase:null, playerTurn : null};
            this.handleEvent = this.handleEvent.bind(this);
        }
        
    async componentDidMount(){
        try{
            let response = await fetch(`http://localhost:8080/territories`);
            if(response.ok){
                let data = await response.json()
                let dataTreated = data.map((territories) => { return [territories.id, territories.name, territories.pawn, territories.player.name]});
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
    handleEvent(phase){
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
                <div className ="carte">
                        <div className="tout">
                        <div id="Cerveau"  onClick={this.handleClick}>
                        
                        
                        
                        <div id="Cerveau1">{this.state.isLoaded ? "(ID = "+this.state.territories[0][0]+")   ( Nom = "+this.state.territories[0][1]+")     ( pawns = "+this.state.territories[0][2]+") (joueur = "+this.state.territories[0][3]+")": <div>Loading..</div>}</div>
                        <div id="Cerveau2">{this.state.isLoaded ? "(ID = "+this.state.territories[1][0]+")     ( Nom = "+this.state.territories[1][1]+")     ( pawns = "+this.state.territories[1][2]+") (joueur = "+this.state.territories[1][3]+")": <div>Loading..</div>}</div>
                        <div id="Cerveau3">{this.state.isLoaded ? "(ID = "+this.state.territories[2][0]+")     ( Nom = "+this.state.territories[2][1]+")     ( pawns = "+this.state.territories[2][2]+") (joueur = "+this.state.territories[2][3]+")": <div>Loading..</div>}</div>
                        <div id="Cerveau4">{this.state.isLoaded ? "(ID = "+this.state.territories[3][0]+")     ( Nom = "+this.state.territories[3][1]+")     ( pawns = "+this.state.territories[3][2]+") (joueur = "+this.state.territories[3][3]+")": <div>Loading..</div>}</div>
                        <div id="Cerveau5">{this.state.isLoaded ? "(ID = "+this.state.territories[4][0]+")     ( Nom = "+this.state.territories[4][1]+")     ( pawns = "+this.state.territories[4][2]+") (joueur = "+this.state.territories[4][3]+")": <div>Loading..</div>}</div>
                        <div id="Cerveau6">{this.state.isLoaded ? "(ID = "+this.state.territories[5][0]+")     ( Nom = "+this.state.territories[5][1]+")     ( pawns = "+this.state.territories[5][2]+") (joueur = "+this.state.territories[5][3]+")": <div>Loading..</div>}</div>
                        </div>

                       
                        <div id="Poumondroit" onClick={this.handleClick}>
                        <div id="Poumondroit1">{this.state.isLoaded ?  "(ID = "+this.state.territories[6][0]+")     ( Nom = "+this.state.territories[6][1]+")     ( pawns = "+this.state.territories[6][2]+") (joueur = "+this.state.territories[6][3]+")": <div>Loading..</div>}</div>
                        <div id="Poumondroit2">{this.state.isLoaded ?  "(ID = "+this.state.territories[7][0]+")     ( Nom = "+this.state.territories[7][1]+")     ( pawns = "+this.state.territories[7][2]+") (joueur = "+this.state.territories[7][3]+")": <div>Loading..</div>}</div>
                        <div id="Poumondroit3">{this.state.isLoaded ?  "(ID = "+this.state.territories[8][0]+")     ( Nom = "+this.state.territories[8][1]+")     ( pawns = "+this.state.territories[8][2]+") (joueur = "+this.state.territories[8][3]+")": <div>Loading..</div>}</div>
                        </div>

                       
                        <div id="Poumongauche" onClick={this.handleClick}>
                        <div id="Poumongauche1" >{this.state.isLoaded ?  "(ID = "+this.state.territories[9][0]+")       ( Nom = "+this.state.territories[9][1]+")     ( pawns = "+this.state.territories[9][2]+")  (joueur = "+this.state.territories[9][3]+")": <div>Loading..</div>}</div>
                        <div id="Poumongauche2" >{this.state.isLoaded ?  "(ID = "+this.state.territories[10][0]+")     ( Nom = "+this.state.territories[10][1]+")     ( pawns = "+this.state.territories[10][2]+") (joueur = "+this.state.territories[10][3]+")": <div>Loading..</div>}</div>
                        <div id="Poumongauche3" >{this.state.isLoaded ?  "(ID = "+this.state.territories[11][0]+")     ( Nom = "+this.state.territories[11][1]+")     ( pawns = "+this.state.territories[11][2]+") (joueur = "+this.state.territories[11][3]+")": <div>Loading..</div>}</div>
                        </div>

                       
                        <div id="Coeur" onClick={this.handleClick}>
                        <div id="Coeur1" >{this.state.isLoaded ?  "(ID = "+this.state.territories[12][0]+")     ( Nom = "+this.state.territories[12][1]+")     ( pawns = "+this.state.territories[12][2]+") (joueur = "+this.state.territories[12][3]+")": <div>Loading..</div>}</div>
                        <div id="Coeur2" >{this.state.isLoaded ?  "(ID = "+this.state.territories[13][0]+")     ( Nom = "+this.state.territories[13][1]+")     ( pawns = "+this.state.territories[13][2]+") (joueur = "+this.state.territories[13][3]+")": <div>Loading..</div>}</div>
                        </div>
                        
                        
                        <div id="Foie" onClick={this.handleClick}>
                        <div id="Foie1" >{this.state.isLoaded ?  "(ID = "+this.state.territories[14][0]+")     ( Nom = "+this.state.territories[14][1]+")     ( pawns = "+this.state.territories[14][2]+") (joueur = "+this.state.territories[14][3]+")": <div>Loading..</div>}</div>
                        <div id="Foie2" >{this.state.isLoaded ?  "(ID = "+this.state.territories[15][0]+")     ( Nom = "+this.state.territories[15][1]+")     ( pawns = "+this.state.territories[15][2]+") (joueur = "+this.state.territories[15][3]+")": <div>Loading..</div>}</div>
                        <div id="Foie3" >{this.state.isLoaded ?  "(ID = "+this.state.territories[16][0]+")      ( Nom = "+this.state.territories[16][1]+")    ( pawns = "+this.state.territories[16][2]+") (joueur = "+this.state.territories[16][3]+")": <div>Loading..</div>}</div>
                        </div>

                       
                        <div id="Estomac" onClick={this.handleClick}>
                        <div id="Estomac1" >{this.state.isLoaded ?  "(ID = "+this.state.territories[17][0]+")     ( Nom = "+this.state.territories[17][1]+")     ( pawns = "+this.state.territories[17][2]+") (joueur = "+this.state.territories[17][3]+")": <div>Loading..</div>}</div>
                        <div id="Estomac2" >{this.state.isLoaded ?  "(ID = "+this.state.territories[18][0]+")     ( Nom = "+this.state.territories[18][1]+")     ( pawns = "+this.state.territories[18][2]+") (joueur = "+this.state.territories[18][3]+")": <div>Loading..</div>}</div>
                        </div>

                        
                        <div id="Rein" onClick={this.handleClick}>
                        <div id="Rein1" >{this.state.isLoaded ?  "(ID = "+this.state.territories[19][0]+")     ( Nom = "+this.state.territories[19][1]+")     ( pawns = "+this.state.territories[19][2]+") (joueur = "+this.state.territories[19][3]+")": <div>Loading..</div>}</div>
                        <div id="Rein2" >{this.state.isLoaded ?  "(ID = "+this.state.territories[20][0]+")     ( Nom = "+this.state.territories[20][1]+")     ( pawns = "+this.state.territories[20][2]+") (joueur = "+this.state.territories[20][3]+")": <div>Loading..</div>}</div>
                        <div id="Rein3" >{this.state.isLoaded ?  "(ID = "+this.state.territories[21][0]+")     ( Nom = "+this.state.territories[21][1]+")     ( pawns = "+this.state.territories[21][2]+") (joueur = "+this.state.territories[21][3]+")": <div>Loading..</div>}</div>
                        </div>
                        
                       
                        <div id="Grosintestin" onClick={this.handleClick}>
                        <div id="Grosintestin1" >{this.state.isLoaded ?  "(ID = "+this.state.territories[22][0]+")     ( Nom = "+this.state.territories[22][1]+")     ( pawns = "+this.state.territories[22][2]+") (joueur = "+this.state.territories[22][3]+")": <div>Loading..</div>}</div>
                        <div id="Grosintestin2" >{this.state.isLoaded ?  "(ID = "+this.state.territories[23][0]+")     ( Nom = "+this.state.territories[23][1]+")     ( pawns = "+this.state.territories[23][2]+") (joueur = "+this.state.territories[23][3]+")": <div>Loading..</div>}</div>
                        <div id="Grosintestin3" >{this.state.isLoaded ?  "(ID = "+this.state.territories[24][0]+")     ( Nom = "+this.state.territories[24][1]+")     ( pawns = "+this.state.territories[24][2]+") (joueur = "+this.state.territories[24][3]+")": <div>Loading..</div>}</div>
                        </div>

                        
                        <div id="Petitintestin" onClick={this.handleClick}>
                        <div id="Petitintestin1" >{this.state.isLoaded ?  "(ID = "+this.state.territories[25][0]+")     ( Nom = "+this.state.territories[25][1]+")     ( pawns = "+this.state.territories[25][2]+") (joueur = "+this.state.territories[25][3]+")": <div>Loading..</div>}</div>
                        <div id="Petitintestin2" >{this.state.isLoaded ?  "(ID = "+this.state.territories[26][0]+")     ( Nom = "+this.state.territories[26][1]+")     ( pawns = "+this.state.territories[26][2]+") (joueur = "+this.state.territories[26][3]+")": <div>Loading..</div>}</div>
                        <div id="Petitintestin3" >{this.state.isLoaded ?  "(ID = "+this.state.territories[27][0]+")     ( Nom = "+this.state.territories[27][1]+")     ( pawns = "+this.state.territories[27][2]+") (joueur = "+this.state.territories[27][3]+")": <div>Loading..</div>}</div>
                        <div id="Petitintestin4" >{this.state.isLoaded ?  "(ID = "+this.state.territories[28][0]+")     ( Nom = "+this.state.territories[28][1]+")     ( pawns = "+this.state.territories[28][2]+") (joueur = "+this.state.territories[28][3]+")": <div>Loading..</div>}</div>
                    </div>
                    </div>
                    {this.state.roundPhase != "INITIALIZE" ? <PlayerTurn playerturn={this.state.playerTurn}/> : ""}
                    {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "PLACEPAWN" ? <PlacePawnInterface updatephase={this.handleEvent}  /> : ""}
                    {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "ATTACK" ? <AttackPhase updatephase={this.handleEvent}  /> : ""}
                    {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "MOVEFORTIFY" ? <MoovFortify updatephase={this.handleEvent} /> : ""}
                </div>
              
        );
    }
   
    
}