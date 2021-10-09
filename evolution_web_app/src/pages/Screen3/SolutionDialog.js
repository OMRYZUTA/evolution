import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import {makeStyles} from "@mui/styles";
import React, {useContext, useEffect, useState} from "react";
import Typography from "@mui/material/Typography";
import TeacherView from "./TeacherView";
import * as Screen3Services from "../../services/Screen3Services";
import {TimetableContext} from "../../components/TimetableContext";
import {getBestSolution} from "../../services/Screen3Services";
import CircularIndeterminate from "../../components/CircularIndeterminate";
import SolutionTabs from "./SolutionTabs";

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

const SolutionDialog = ({handleClose, days, hours, solution,teachers, schoolClasses}) => {
    const {currentTimetableID} = useContext(TimetableContext);
    const classes = useStyles();

    return (
        <Dialog onClose={handleClose} open={true} fullWidth={true} maxWidth={"xl"}>
            <DialogTitle>Best Solution</DialogTitle>
            {solution? <SolutionTabs quintets={Object.keys(solution.solutionQuintets).map(key => solution.solutionQuintets[key])} hours={hours} days={days} teachersObject={teachers} schoolClassesObject={schoolClasses}/>: <CircularIndeterminate/>}
        </Dialog>
    );
}
export default SolutionDialog;