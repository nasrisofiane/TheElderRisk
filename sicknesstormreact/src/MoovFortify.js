import React from 'react';
import './move_fortify.css';

class MoovFortify extends React.Component{ 

    state = {isLoaded : false, numberOfPawn : 1}

    closeMoveFortifyPhase = async () =>{
        try{
            let response = await fetch(`http://localhost:8080/closemovefortifystep`);
            if(response.ok){
                let data = await response.text()
                console.log(data);
                await this.props.updatephase();
                await this.props.cleanSelected();
                throw new Error(response.statusText);
            }      
        }
        catch(err){ 
        }
    }
    
    sendMovePawnsToServer = async () => {
        if(this.props.territoryAttackerSelected != null && this.props.territoryDefenderSelected != null && this.state.numberOfPawn != null){
            try{
                let response = await fetch(`http://localhost:8080/movefortify/${this.props.territoryAttackerSelected[0][0]}/${this.props.territoryDefenderSelected[0][0]}/${parseInt(this.state.numberOfPawn)}`);
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

    render(){
        return(
            <div className="container-movepawns-phase">
                <div id="preview_fight"><p id="attacker-choice">{this.props.territoryAttackerSelected != null ? this.props.territoryAttackerSelected[0][1] :"Select a territory"} </p>VS <p id="defender-choice">{this.props.territoryDefenderSelected != null ? this.props.territoryDefenderSelected[0][1] :"Select a territory"} </p> </div>
                <h2>Move Pawns phase</h2>
                <div className="movepawns-phase">
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
