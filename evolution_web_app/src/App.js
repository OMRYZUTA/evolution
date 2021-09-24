import './App.css';
import React, {useMemo} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/signup/SignUp' //TODO - delete later
import Screen2 from './pages/Screen2'
import {UserContext} from "./components/UserContext";
import Screen3 from "./pages/Screen3";

export default function App() {
    const [currentUser, setCurrentUser] = React.useState(null);
    const providerValue = useMemo(()=>({currentUser,setCurrentUser}),[currentUser,setCurrentUser]);
    return (
        <UserContext.Provider value={providerValue}>
            <Router>
                <div>

                    {/*
          A <Switch> looks through all its children <Route>
          elements and renders the first one whose path
          matches the current URL. only one
          of them to render at a time
        */}
                    <Switch>
                        <Route exact path="/server_Web_exploded">
                            <Login />
                        </Route>
                        <Route path="/server_Web_exploded/screen1">
                            <Login />
                        </Route>
                        <Route path="/server_Web_exploded/signup">
                            <SignUp/>
                        </Route>
                        <Route path="/server_Web_exploded/screen2">
                            {currentUser?<Screen2/>:<Login/>}
                        </Route>
                        <Route path="/server_Web_exploded/screen3">
                            {currentUser?<Screen3/>:<Login/>}
                        </Route>
                    </Switch>
                </div>
            </Router>
        </UserContext.Provider>
    );
}
