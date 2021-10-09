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
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AccordionDetails from "@mui/material/AccordionDetails";
import Grid from "@mui/material/Grid";
import RulesScoreContainer from "./RulesScoreContainer";
import * as Utils from "../../Utils/Utils"
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

const SolutionDialog = ({handleClose, days, hours, solution, teachers, schoolClasses}) => {
    const {currentTimetableID} = useContext(TimetableContext);
    const classes = useStyles();
    console.log(solution.scorePerRule);

    return (
        <Dialog onClose={handleClose} open={true} fullWidth={true} maxWidth={"xl"}>
            <DialogTitle>Best Solution</DialogTitle>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>Rules score summary</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <RulesScoreContainer rulesScores={solution.scorePerRule}/>
                </AccordionDetails>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>Solution views</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    {solution ? <SolutionTabs
                            quintets={Object.keys(solution.solutionQuintets).map(key => solution.solutionQuintets[key])}
                            hours={hours} days={days} teachersObject={teachers} schoolClassesObject={schoolClasses}/> :
                        <CircularIndeterminate/>}
                </AccordionDetails>
            </Accordion>

        </Dialog>
    );
}
export default SolutionDialog;