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
                output: JSON.stringify(JSON.parse(evt.data), null, 2)
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
                    <h1>Reactive Search Poc</h1>
                    Type a country or a name to get some results .. (1 million records available)
                </div>
                <div>
                    <input type="text" value={this.state.snippet} onChange={this.autocomplete}/>
                </div>
                <div>
                    <pre>{this.state.output}</pre>
                </div>
            </div>
        )
    }
}

ReactDOM.render(
    <AutocompleteComponent/>,
    document.getElementById('content')
);