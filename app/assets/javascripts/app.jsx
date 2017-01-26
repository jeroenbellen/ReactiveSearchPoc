"use strict";

class AutocompleteComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            snippet: "",
            output: ""
        };
        this.autocomplete = this.autocomplete.bind(this);

        this.ws = new WebSocket("ws://localhost:9000/ws/search");
        this.ws.onmessage = evt => {
            this.setState({
                output: evt.data
            });
        };
    }

    autocomplete(event) {
        event.preventDefault();

        this.ws.send(event.target.value);

        this.setState({
            snippet: event.target.value
        });
    }

    render() {
        return (
            <div>
                <div>
                    <input type="text" value={this.state.snippet} onChange={this.autocomplete}/>
                </div>
                <div>
                    {this.state.output}
                </div>
            </div>
        )
    }
}

ReactDOM.render(
    <AutocompleteComponent/>,
    document.getElementById('content')
);