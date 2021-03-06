import * as React from 'react';
import {useContext, useState} from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import {useHistory} from "react-router-dom";
import {TimetableContext} from "../../components/TimetableContext";

const bull = (
    <Box
        component="span"
        sx={{display: 'inline-block', mx: '2px', transform: 'scale(0.8)'}}
    >
        •
    </Box>
);

const SCREEN3URL = "/evolution/screen3";

export default function Summary({data}) {
    const [localTimetable, setLocalTimetable] = useState(data.ID);
    const {currentTimetableID, setCurrentTimetableID} = useContext(TimetableContext);
    const history = useHistory();

    const routeChange = () => {
        history.push(SCREEN3URL);
    }

    const handleTimeTableClicked = async () => {
        document.cookie = `timetableID=${localTimetable};path=/`
        await setCurrentTimetableID(localTimetable)
        routeChange()
    }

    return (
        <Card sx={{minWidth: 275}}>
            <CardContent>
                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                    Uploaded by: {data.uploadedBy}
                </Typography>
                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                    Problem ID:{data.ID}
                </Typography>
                <Typography variant="body2">
                    {bull}{data.days} days, {data.hours} hours
                </Typography>
                <Typography variant="body2">
                    {bull}{data.numOfClasses} classes, {data.numOfTeachers} teachers
                </Typography>
                <Typography variant="body2">
                    {bull}{data.numOfHardRules} hard rules, {data.numOfSoftRules} soft rules
                </Typography>
                <Typography variant="body2">
                    {bull}{data.numOfUsersSolving} users are trying to solve it
                </Typography>
                <Typography variant="body2">
                    {bull}Max fitness score (so far): {data.bestScore.toFixed(4)}
                </Typography>

            </CardContent>
            <CardActions>
                <Button size="small" onClick={handleTimeTableClicked}>Open</Button>
            </CardActions>
        </Card>
    );
}
