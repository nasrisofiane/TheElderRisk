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
                this.setState({
                    isLoaded : true,
                    territories : data.map((territories, {id, name}) => { return {id : territories.id, name :territories.name}})
                })
                throw new Error(response.statusText);
            }
                
        }
        catch(err){
            console.log(err);
        }
        
    }

    

    render(){
        return(
            <div id={this.props.id}>
                <label for={this.props.name}> {this.props.name.toUpperCase()} </label>
                <select onClick={()=> {this.componentDidMount()}} name="territories" className="territories" id={this.props.name} onChange={e => this.props.action(e.target.options[e.target.selectedIndex])}>
                    {this.state.isLoaded ? this.state.territories.map((territories) => <option value={territories.id}>{territories.name.replace(/_/g, ' ')}</option>) : <option>Loading..</option>}
                </select>
            </div>
        );
    }
}