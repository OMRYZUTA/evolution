import './App.css';
import React, {useMemo} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Login from './pages/Login';
import Screen2 from './pages/Screen2'
import {UserContext} from "./components/UserContext";
import Screen3 from "./pages/Screen3";
import {TimetableContext} from "./components/TimetableContext";
import '@devexpress/dx-react-chart-bootstrap4/dist/dx-react-chart-bootstrap4.css';

const getCookieDetail = (name) => {
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length + 1, c.length);
        }
    }
    return "";
}

export default function App() {
    const [currentUser, setCurrentUser] = React.useState(getCookieDetail("username"));
    const [currentTimetableID, setCurrentTimetableID] = React.useState(Number.parseInt(getCookieDetail("timetableID"), 10));
    const userProviderValue = useMemo(() => ({currentUser, setCurrentUser}), [currentUser, setCurrentUser]);
    const timetableProviderValue = useMemo(() => ({
        currentTimetableID,
        setCurrentTimetableID
    }), [currentTimetableID, setCurrentTimetableID]);
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
                            <Route exact path="/evolution">
                                <Login/>
                            </Route>
                            <Route path="/evolution/screen1">
                                <Login/>
                            </Route>
                            <Route path="/evolution/screen2">
                                {currentUser ? <Screen2/> : <Login/>}
                            </Route>
                            <Route path="/evolution/screen3">
                                {currentUser ? <Screen3/> : <Login/>}
                            </Route>
                        </Switch>
                    </div>
                </Router>
            </UserContext.Provider>
        </TimetableContext.Provider>
    );
}
