"use strict";

class AutocompleteComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            snippet: "",
            output: ""
        };
        this.autocomplete = this.autocomplete.bind(this);

        this.openSearchSocket();
    }

    openSearchSocket() {
        const uri = "ws://" + window.location.href.replace('http://', '').replace('https://', '') + "ws/search";
        console.log("Opening new socket uri: " + uri);

        this.ws = new WebSocket(uri);
        this.ws.onmessage = evt => {
            this.setState({
                output: JSON.parse(evt.data)
            });
        };
    }

    autocomplete(event) {
        event.preventDefault();

        if (this.ws.readyState === 2 || this.ws.readyState === 3) {
            console.log("WS closed, let's reopen");
            this.openSearchSocket();
        }

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
                    <table>
                        <thead>
                        <tr>
                            <th>Id</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Country</th>
                        </tr>
                        </thead>
                        {(this.state.output || []).map((row, i) =>
                            <tr key={i}>
                                <td>{row.id}</td>
                                <td>{row.fullName}</td>
                                <td>{row.email}</td>
                                <td>{row.country}</td>
                            </tr>
                        )}
                    </table>
                </div>
            </div>
        )
    }
}

ReactDOM.render(
    <AutocompleteComponent/>,
    document.getElementById('content')
);