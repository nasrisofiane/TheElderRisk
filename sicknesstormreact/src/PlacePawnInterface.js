import React from 'react';
import AllTerritories from './AllTerritories';
import './place_pawn_interface.css';

class PlacePawnInterface extends React.Component{ 

    state = {pawn: 1, messageError:null}

    sendAddPawnsToServer = async () => {
        if(this.props.territoryAttackerSelected != null && this.state.pawn != null){
            try{
                let response = await fetch(`http://localhost:8080/addpawn/${parseInt(this.props.territoryAttackerSelected[0])}/${parseInt(this.state.pawn)}`);
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
    } 

   

    addPawnsInputs =({target:{id, value}}) => {
        this.setState({pawn:value});
        console.log("MovePawns "+ this.state.pawn);
    }

    render (){
        return(
        <div className="container-addpawns-phase">
            <div>{this.props.territoryAttackerSelected != null ? this.props.territoryAttackerSelected[1] : "Select a territory"}</div>
            <h2>Add Pawns phase</h2>
                <div className="addpawns-phase">
                    <div className="number-addpawns-container">
                        <h3>Number of pawns</h3>
                        <input id="number-of-pawns" type="number" value={this.state.pawn} min="1" onChange={this.addPawnsInputs}/>
                    </div>
                    <button id="add-pawns-button" onClick={this.sendAddPawnsToServer}>Add your pawns</button>
                </div>
                <div>{this.state.messageError != null ? this.state.messageError : ""}</div>
        </div> 
            )      
    }
}



  
export default PlacePawnInterface;
 
