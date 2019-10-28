import React from 'react';
import AllTerritories from './AllTerritories';
import './place_pawn_interface.css';

class PlacePawnInterface extends React.Component{ 

    state = {pawn: 1, territory:null}

    sendAddPawnsToServer = async () => {
        if(this.state.territory != null && this.state.pawn != null){
            try{
                let response = await fetch(`http://localhost:8080/addpawn/${parseInt(this.state.territory)}/${parseInt(this.state.pawn)}`);
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
        else{
            alert("Be sure that you have selected the territory");
        }
    } 

    territoriesInputs = (territorySelected, listName) => {
        this.setState({territory : territorySelected.value});
    }

    addPawnsInputs =({target:{id, value}}) => {
        this.setState({pawn:value});
        console.log("MovePawns "+ this.state.pawn);
    }

    render (){
        return(
        <div className="container-addpawns-phase">
            <h2>Add Pawns phase</h2>
                <div className="addpawns-phase">
                    <AllTerritories action={this.territoriesInputs} id="list-territories-addpawns" name="Territories"/>
                    <div className="number-addpawns-container">
                        <h3>Number of pawns</h3>
                        <input id="number-of-pawns" type="number" value={this.state.pawn} min="1" onChange={this.addPawnsInputs}/>
                    </div>
                    <button id="add-pawns-button" onClick={this.sendAddPawnsToServer}>Add your pawns</button>
                </div>
        </div> 
            )      
    }
}



  
export default PlacePawnInterface;
 
