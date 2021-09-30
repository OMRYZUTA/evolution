import {makeStyles} from "@mui/styles";
import Paper from "@mui/material/Paper";
import React from 'react';
import Typography from "@mui/material/Typography";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AccordionDetails from "@mui/material/AccordionDetails";
import Grid from "@mui/material/Grid";
import {TextField} from "@mui/material";

const useStyles = makeStyles((theme) => ({
    root: {
        spacing: 2,
        justifyContent: "space-between"
    },
    settings: {
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const TimetableDetails = ({timetable}) => {
    //days, hours subjects, teachers, classes, rules
    const classes = useStyles();
    console.log(timetable);
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
                                    Table ID: {timetable.ID}
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
                    <Grid container direction={"column"}>
                        {timetable.rules.map(rule=>{
                            console.log(rule);

                            return(
                                <Grid item>
                                    <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                        Days: {} Hours {timetable.hours}
                                    </Typography>
                                </Grid>
                            )
                        })}
                    </Grid>
                </AccordionDetails>
            </Accordion>


            {/*{timetable.subjects.map(subject => <Typography>{subject.id} {subject.name}</Typography>)}*/}

        </Paper>
    )
        ;
}

export default TimetableDetails;