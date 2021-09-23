import * as React from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

const bull = (
    <Box
        component="span"
        sx={{ display: 'inline-block', mx: '2px', transform: 'scale(0.8)' }}
    >
        •
    </Box>
);

export default function Summary({data}) {
    return (
        <Card sx={{ minWidth: 275 }}>
            <CardContent>
                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                    Uploaded by {data.username}
                </Typography>
                <Typography variant="body2">
                   {bull}{data.days} days, {data.hours} hours
                </Typography>
                <Typography variant="body2">
                    {bull}{data.schoolclasses} classes, {data.teachers} teachers
                </Typography>
                <Typography variant="body2">
                    {bull}{data.hardRules} hard rules, {data.softRules} soft rules
                </Typography>
                <Typography variant="body2">
                    {bull}{data.solvingUsers} Users are trying to solve it
                </Typography>
                <Typography variant="body2">
                    {bull}{data.maxFitnessSoFar} -max fitness so far
                </Typography>

            </CardContent>
            <CardActions>
                <Button size="small">Open</Button>
            </CardActions>
        </Card>
    );
}
