import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import {makeStyles} from "@mui/styles";
import React, {useContext, useEffect, useState} from "react";
import Typography from "@mui/material/Typography";
import TeacherView from "./TeacherView";
import * as Screen3Services from "../../services/Screen3Services";
import {TimetableContext} from "../../components/TimetableContext";
import {getBestSolution} from "../../services/Screen3Services";

const useStyles = makeStyles((theme) => ({
    grid: {
        margin: 0,
        width: '100%',
        backgroundColor: '#FFFFC5',//yellow
        alignItems: 'stretch',
    },
    root: {
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
        minHeight: "100vh",
    },
    settings: {
        spacing: 2,
        justifyContent: "space-between",
        width: '100%',
        height: 400,
        maxWidth: 300,
        backgroundColor: "#D3D3D3", //light gray
    },
    actions: {
        padding: 20,
        margin: 20,
        justifyContent: 'space-evenly',
    },
    tempGrid: {
        padding: 20,
        margin: 20,
        backgroundColor: "pink",
    },
}));

const SolutionDialog = ({handleClose}) => {
    const {currentTimetableID} = useContext(TimetableContext);
    const classes = useStyles();
    const [solution,setSolution]= useState({});

    useEffect(() => {
        const fetchAllData = async () => {
            // calling all API calls in parallel, and waiting until they ALL finish before setting
            try {
                console.log({currentTimetableID})
                const solutionPayload = await Screen3Services.getBestSolution(currentTimetableID);
                setSolution(solutionPayload);
                console.log("solution received:");
                console.log({solutionPayload});
            } catch (e) {
                console.log("inside solution dialog", e);
                // setAlertText('Failed initializing app, please reload page');
            } finally {
                // setIsFetching(false);
            }
        };

        fetchAllData();
    }, []);

    return (
        <Dialog onClose={handleClose} open={true} fullWidth={true} maxWidth={"xl"}>
            <DialogTitle>Best Solution</DialogTitle>
           <TeacherView solution={solution}/>
        </Dialog>
    );
}

export default SolutionDialog;