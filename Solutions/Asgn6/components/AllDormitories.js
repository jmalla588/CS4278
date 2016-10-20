// structure followed from React tutorial here:
// https://facebook.github.io/react/docs/thinking-in-react.html

var React = require('react');
var AllDormitories = React.createClass({

    handleData(data) {
        this.setState({dorms: data})
    },

    shouldComponentUpdate(nextProps, nextState) {
        return nextState !== this.state;
    },

    componentWillMount() {
        var self = this;
        var reqdata = {};
        self.setState({dorms: []});
        $.ajax({
            type: 'get',
            url: 'http://localhost:8080/api/dormitories'
        }).done(function(data) {
            reqdata = data;
            self.handleData(data);
            console.log('successfully retrieved dorm data');
        }).fail(function(jqXhr) {
            alert('failed to access dorm data.');
            console.log('failed to access dorm data');
        });
    },

    render() {
        var dorms = [];
        this.state.dorms.forEach(function(dorm) {
            dorms.push(<DormRow dID={dorm.id} dName={dorm.dormName} />)
        });

        return (
            <table>
                <tr>
                <th>dormID</th>
                <th>dormName</th>
                </tr>
                { dorms }
            </table>
        );
    }
});

var DormRow = React.createClass({
    render() {
        return(
            <tr>
                <td>{ this.props.dID }</td>
                <td>{ this.props.dName }</td>
            </tr>
        );
    }
});

module.exports = AllDormitories;
