import React from 'react';
import AllTerritories from './AllTerritories';
import './place_pawn_interface.css';

class PlacePawnInterface extends React.Component{ 

    state = {pawn: 1, messageError:null, changeClass:"hide-phase-popup", styles:{display:"block"}}


    sendAddPawnsToServer = async () => {
        if(this.props.territoryAttackerSelected != null && this.state.pawn != null){
            try{
                let response = await fetch(`http://localhost:8080/addpawn/${parseInt(this.props.territoryAttackerSelected[0][0])}/${parseInt(this.state.pawn)}`);
                if(response.ok){
                    let data = await response.text()
                    this.setState({messageError:data});
                    console.log(data);
                    await this.props.updatephase();
                    throw new Error(response.statusText);
                }
                    
            }
            catch(err){
            }
        }
        else{
            alert("Be sure that you have selected the territory");
        }
        await this.displayPhasePopUp();
    } 

    displayPhasePopUp = () =>{
        if(this.props.territoryAttackerSelected != null){
            if(this.state.changeClass == "hide-phase-popup"){
                this.setState({changeClass:"", styles:{display:"none"}});
            }
            else{
                this.setState({changeClass:"hide-phase-popup", styles:{display:"block"}});
            }
        }
        else{
            alert("Select a territory first");
        }
       
            
    }

    addPawnsInputs =({target:{id, value}}) => {
        this.setState({pawn:value});
        console.log("MovePawns "+ this.state.pawn);
    }

    render (){
        return(
            <div>
                <div className={"infos-phase-popup "+ this.state.changeClass}>
                    <div className="form-phase">
                        <div className="selected-territory-popup">How many pawns do you wants to add on <strong >{this.props.territoryAttackerSelected != null ? this.props.territoryAttackerSelected[0][1] : "Select a territory"}</strong></div>
                        <div className="addpawns-phase">
                            <div className="number-addpawns-container">
                                <input id="number-of-pawns" type="number" value={this.state.pawn} min="1" onChange={this.addPawnsInputs}/>
                            </div>
                            <button id="add-pawns-button" onClick={this.sendAddPawnsToServer}>Add your pawns</button>
                        </div>
                    </div>
                </div>
                <div className={"container-addpawns-phase " } style={this.state.styles}>
                <h2>Add Pawns phase</h2>
                <div>{this.state.messageError != null ? this.state.messageError : ""}</div>
                <button id="add-pawns-button-popup" onClick={() => this.displayPhasePopUp()}>Add your pawns</button>
        </div>
            </div>
            )      
    }
}

export default PlacePawnInterface;
 
