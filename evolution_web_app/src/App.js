import './App.css';
import React, {useMemo} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/signup/SignUp' //TODO - delete later
import Screen2 from './pages/Screen2'
import {UserContext} from "./components/UserContext";
import Screen3 from "./pages/Screen3";
import {TimetableContext} from "./components/TimetableContext";

export default function App() {
    const [currentUser, setCurrentUser] = React.useState(document.cookie);
    const [currentTimetableID, setCurrentTimetableID] = React.useState(null);
    const userProviderValue = useMemo(() => ({currentUser, setCurrentUser}), [currentUser, setCurrentUser]);
    const timetableProviderValue = useMemo(() => ({currentTimetable: currentTimetableID, setCurrentTimetable: setCurrentTimetableID}), [currentTimetableID, setCurrentTimetableID]);
    return (
        <TimetableContext.Provider value={timetableProviderValue}>
            <UserContext.Provider value={userProviderValue}>
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
                                <Login/>
                            </Route>
                            <Route path="/server_Web_exploded/screen1">
                                <Login/>
                            </Route>
                            <Route path="/server_Web_exploded/signup">
                                <SignUp/>
                            </Route>
                            <Route path="/server_Web_exploded/screen2">
                                {currentUser ? <Screen2/> : <Login/>}
                            </Route>
                            <Route path="/server_Web_exploded/screen3">
                                {currentUser ? <Screen3/> : <Login/>}
                            </Route>
                        </Switch>
                    </div>
                </Router>
            </UserContext.Provider>
        </TimetableContext.Provider>
    );
}
