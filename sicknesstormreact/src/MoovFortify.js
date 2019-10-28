import React from 'react';
import AllTerritories from './AllTerritories';
import './move_fortify.css';

class MoovFortify extends React.Component{ 

    state = {isLoaded : false, territoryStart : null , territoryArrive : null , numberOfPawn : 1}

    closeMoveFortifyPhase = async () =>{
        try{
            let response = await fetch(`http://localhost:8080/closemovefortifystep`);
            if(response.ok){
                let data = await response.text()
                console.log(data);
                await this.props.updatephase();
                throw new Error(response.statusText);
            }      
        }
        catch(err){ 
        }
    }
    
    sendMovePawnsToServer = async () => {
        if(this.state.territoryStart != null && this.state.territoryArrive != null && this.state.numberOfPawn != null){
            try{
                let response = await fetch(`http://localhost:8080/movefortify/${parseInt(this.state.territoryStart)}/${parseInt(this.state.territoryArrive)}/${parseInt(this.state.numberOfPawn)}`);
                if(response.ok){
                    let data = await response.text()
                    this.setState({isLoaded:true});
                    console.log(data);
                    await this.props.updatephase();
                    throw new Error(response.statusText);
                }
                    
            }
            catch(err){
            }
        }
        else{
            alert("Be sure that you have selected the territories");
        }
        
        
    } 

    movePawnsInputs =({target:{id, value}}) => {
            this.setState({numberOfPawn:value});
            console.log("MovePawns "+ this.state.numberOfPawn);
    
    }

    territoriesInputs = (territorySelected, listName) => {
        if(listName == "start"){
            this.setState({territoryStart : territorySelected.value});
            

        }
        if(listName == "arrive"){
            this.setState({territoryArrive : territorySelected.value});
            
        }
    }
    

      render(){
        return(
            <div className="container-movepawns-phase">
                <h2>Move Pawns phase</h2>
                <div className="movepawns-phase">
                    <AllTerritories action={this.territoriesInputs} id="list-territories-start" name="start"/>
                    <AllTerritories action={this.territoriesInputs} id="list-territories-arrive" name="arrive"/>
                    <div className="number-pawns-container">
                        <h3>Number of pawns</h3>
                        <input id="number-of-pawns" type="number" value={this.state.numberOfPawn} min="1" onChange={this.movePawnsInputs}/>
                    </div>
                      <button id="move-pawns-button" onClick={this.sendMovePawnsToServer}>Move your pawns</button>
                      <button id="close-movefortify-button" onClick={this.closeMoveFortifyPhase}>Finish your round</button>
                </div>
            </div>
        );
    }
}



  
export default MoovFortify;
