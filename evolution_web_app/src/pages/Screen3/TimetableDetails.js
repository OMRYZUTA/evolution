import Accordion from "@mui/material/Accordion";
import AccordionDetails from "@mui/material/AccordionDetails";
import AccordionSummary from "@mui/material/AccordionSummary";
import CircularIndeterminate from "../../components/CircularIndeterminate";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Grid from "@mui/material/Grid";
import {makeStyles} from "@mui/styles";
import Paper from "@mui/material/Paper";
import React from 'react';
import Typography from "@mui/material/Typography";

const useStyles = makeStyles((theme) => ({
    root: {
        justifyContent: "space-between"
    },
    settings: {
        justifyContent: "space-between",
        backgroundColor: "#D3D3D3", //light gray
    },
    requirements: {
        padding: 10,
    },
    schoolClass: {
        border: 1,
        borderBlockColor: "black",
        padding: 10,
        margin: 50,
    }
}));

const TimetableDetails = ({timetable}) => {
    const classes = useStyles();

    // if timetable is undefined
    if (!timetable) {
        return (<CircularIndeterminate/>);
    }

    //days, hours subjects, teachers, classes, rules
    return (
        <Paper>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>General Details</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root} direction={"column"}>
                        <Grid item>
                            <Grid container className={classes.root}>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    Days: {timetable.days}
                                </Typography>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    Hours: {timetable.hours}
                                </Typography>
                            </Grid>
                        </Grid>
                        <Grid item>
                            <Grid container className={classes.root}>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    timetable ID: {timetable.ID}
                                </Typography>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    Uploaded by: {timetable.uploadedBy}
                                </Typography>
                            </Grid>
                        </Grid>
                    </Grid>
                </AccordionDetails>
            </Accordion>

            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>Rules</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container direction={"column"} className={classes.root}>
                        <Typography>
                            Hard rules Weight:
                            {timetable.hardRulesWeight}
                        </Typography>
                        {timetable.rules.map(rule => {
                            return (
                                <Grid item>
                                    <Grid container className={classes.settings}>
                                        <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                            Rule Name: {rule.ruleName}
                                        </Typography>
                                        {rule.ruleName === "Sequentiality" &&
                                        <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                            Total Hours: {rule.totalHours}
                                        </Typography>}
                                        <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                            Rule Type: {rule.ruleType}
                                        </Typography>
                                    </Grid>
                                </Grid>
                            )
                        })}
                    </Grid>
                </AccordionDetails>
            </Accordion>

            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>School classes </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container direction={"column"} className={classes.root}>
                        {Object.keys(timetable.schoolClasses).map((key, index) => {
                            return (
                                <Grid item>
                                    <Grid container direction={"column"} sx={{border: 1}}>
                                        <Grid item>
                                            <Grid container className={classes.settings}>
                                                <Typography>ID: {key} Name
                                                    : {timetable.schoolClasses[key].name}</Typography>

                                            </Grid>
                                        </Grid>
                                        <Grid item>
                                            <Grid container className={classes.settings}>
                                                <Typography>
                                                    Requirements:
                                                </Typography>
                                                {timetable.schoolClasses[key].requirements.map(requirement => {
                                                    return (
                                                        <Grid container direction={"row"}
                                                              className={classes.requirements}>
                                                            <Grid item>
                                                                <Typography>
                                                                    Subject: {requirement.subject.name}, Hours: {requirement.hours}
                                                                </Typography>
                                                            </Grid>
                                                        </Grid>
                                                    )
                                                })
                                                }
                                            </Grid>
                                        </Grid>
                                    </Grid>

                                </Grid>

                            )
                        })}
                    </Grid>
                </AccordionDetails>
            </Accordion>

            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>Subjects</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root} direction={"column"}>
                        {Object.keys(timetable.subjects).map((key, index) => {
                            return (
                                <Grid item>
                                    <Grid container className={classes.settings} direction={"column"}>
                                        <Typography>
                                            {timetable.subjects[key].id}. {timetable.subjects[key].name}
                                        </Typography>
                                    </Grid>
                                </Grid>
                            )
                        })
                        }
                    </Grid>
                </AccordionDetails>
            </Accordion>

            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>Teachers</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root} direction={"column"}>
                        {Object.keys(timetable.teachers).map((key, index) => {
                            return (
                                <Grid item>
                                    <Grid container className={classes.settings} direction={"column"}
                                          sx={{border: 1}}>
                                        <Typography>
                                            {timetable.teachers[key].id}. {timetable.teachers[key].name}
                                        </Typography>
                                        <Typography>
                                            working hours:{timetable.teachers[key].workingHours}
                                        </Typography>
                                        <Typography>
                                            Teaches:
                                        </Typography>
                                        {Object.keys(timetable.teachers[key].subjects).map((subjectKey, index) => {
                                            return (<Typography>
                                                {timetable.teachers[key].subjects[subjectKey].id}. {timetable.teachers[key].subjects[subjectKey].name}
                                            </Typography>)
                                        })}
                                    </Grid>
                                </Grid>
                            )
                        })
                        }
                    </Grid>
                </AccordionDetails>
            </Accordion>
        </Paper>
    );
}

export default TimetableDetails;