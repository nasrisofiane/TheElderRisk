import React from 'react';
import './move_fortify.css';

export default class MoveFortify extends React.Component{ 

    state = {isLoaded : true, numberOfPawn : 1,  messageError:null, changeClass:"hide-phase-popup", styles:{display:"block"}}

    closeMoveFortifyPhase = async () =>{
        await this.props.sendMessageCloseMoveFortifyPhase();
        await this.props.cleanSelected();
    }

    displayPhasePopUp = () =>{
        if(this.props.territoryAttackerSelected != null && this.props.territoryDefenderSelected != null){
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
    
    sendMovePawnsToServer = async () => {
        if(this.props.territoryAttackerSelected != null && this.props.territoryDefenderSelected != null && this.state.numberOfPawn != null){
            this.props.sendMessageToMovePawns(this.props.territoryAttackerSelected[0][0], this.props.territoryDefenderSelected[0][0], this.state.numberOfPawn);
        }
        else{
            alert("Be sure that you have selected the territories");
        }
        this.displayPhasePopUp();
    } 

    movePawnsInputs =({target:{id, value}}) => {
            this.setState({numberOfPawn:value});
            console.log("MovePawns "+ this.state.numberOfPawn);
    
    }

    render(){
        return(
            <div className="container-movepawns-phase">
                <div className={"infos-phase-popup "+ this.state.changeClass}>
                    <div className="form-phase">
                    <div className="selected-territory-popup">How many pawns do you want to move from <strong>{this.props.territoryAttackerSelected != null ? this.props.territoryAttackerSelected[0][1] :"Select a territory"}</strong> to <strong> {this.props.territoryDefenderSelected != null ? this.props.territoryDefenderSelected[0][1] :"Select a territory"} </strong> ? </div>
                        <div className="number-pawns-container">
                            <input id="number-of-pawns" type="number" value={this.state.numberOfPawn} min="1" onChange={this.movePawnsInputs}/>
                        </div>
                        <button id="move-pawns-button" onClick={this.sendMovePawnsToServer}>Move your pawns</button>
                    </div>
                </div>
                <div className="movepawns-content" style={this.state.styles}>
                <p className="error-message">{this.state.messageError != null ? this.state.messageError : "" }</p>
                    <h2>Move Pawns phase</h2>
                    <div className="movepawns-phase">
                        <button id="move-pawns-button" onClick={this.displayPhasePopUp}>Move your pawns</button>
                        <button id="close-movefortify-button" onClick={this.closeMoveFortifyPhase}>Finish your round</button>
                    </div>
                </div>
            </div>
        );
    }
}

