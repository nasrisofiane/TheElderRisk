import React from 'react';
import './all_territories.css';
export default class AllTerritories extends React.Component{
    
    constructor(){
        super();
        this.state = {isLoaded : false ,territories:{}};
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
                throw new Error(response.statusText);
            }
                
        }
        catch(err){
        }
        
    }

    

    render(){
        return(
            <div id={this.props.id}>
                <label for={this.props.name}> {this.props.name.toUpperCase()} </label>
                <select onClick={()=> {this.componentDidMount()}} name="territories" className="territories" id={this.props.name} onChange={e => this.props.action(e.target.options[e.target.selectedIndex], this.props.name)}>
                    <option disabled selected value> Choose a territory </option>
                    {this.state.isLoaded ? this.state.territories.map((territories) => <option value={territories[0]}>{territories[1].replace(/_/g, ' ')}</option>) : <option>Loading..</option>}
                </select>
            </div>
        );
    }
}