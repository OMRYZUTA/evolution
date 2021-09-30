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
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
    },
    settings: {
        width: '100%',
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
                    <Grid container className={classes.root}>

                        <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                            Days: {timetable.days} Hours {timetable.hours}
                        </Typography>
                    </Grid>
                </AccordionDetails>
            </Accordion>



            {/*{timetable.subjects.map(subject => <Typography>{subject.id} {subject.name}</Typography>)}*/}

        </Paper>
    )
        ;
}

export default TimetableDetails;