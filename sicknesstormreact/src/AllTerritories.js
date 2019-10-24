import React from 'react';

export default class AllTerritories extends React.Component{
    
    constructor(){
        super();
        this.state = {isLoaded : false ,territories:{}};
    }
    

    async componentDidMount(){
        fetch(`http://localhost:8080/territories`)
        .then(response => response.json())
        .then(json => this.setState({
            isLoaded : true,
            territories : json.map((territories, {id, name}) => { return {id : territories.id, name :territories.name}}),
            
        }));
    }

    

    render(){
        return(
            <div>
                <label for={this.props.name}> {this.props.name.toUpperCase()} </label>
                <select name="territories" className="territories" id={this.props.name}>
                    {this.state.isLoaded ? this.state.territories.map((territories) => <option value={territories.id}>{territories.name.replace(/_/g, ' ')}</option>) : <div>Loading..</div>}
                </select>
            </div>
        );
    }
}