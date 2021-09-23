import './App.css';
import React from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/signup/SignUp' //TODO - delete later
import Screen2 from './pages/Screen2'
import {UserContext} from "./components/UserContext";

export default function App() {
    const [currentUser, setCurrentUser] = React.useState();

    return (
        <UserContext.Provider value={currentUser}>
            <Router>
                <div>

                    {/*
          A <Switch> looks through all its children <Route>
          elements and renders the first one whose path
          matches the current URL. only one
          of them to render at a time
        */}
                    <Switch>
                        <Route exact path="/">
                            <Login setCurrentUser={setCurrentUser}/>
                        </Route>
                        <Route path="/screen1">
                            <Login setCurrentUser={setCurrentUser}/>
                        </Route>
                        <Route path="/signup">
                            <SignUp/>
                        </Route>
                        <Route path="/screen2">
                            <Screen2/>
                        </Route>
                    </Switch>
                </div>
            </Router>
        </UserContext.Provider>
    );
}
