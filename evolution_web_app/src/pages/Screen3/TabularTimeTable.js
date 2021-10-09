import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import Typography from "@mui/material/Typography";
import TableBody from "@mui/material/TableBody";
import * as React from "react";
import {makeStyles} from "@mui/styles";
import {useEffect, useState} from "react";


const useStyles = makeStyles(() => ({
    table: {
        tableLayout: "fixed",
        width: "100%",
    },
    typography: {
        padding: "2px 1px",
    },
    dropdown: {
        width: "100px",
    },
    cell: {
        padding: "5px",
    }
}));

const convertNumToDay = (day) => {
    let dayName;
    switch (day) {
        case 0:
            dayName = 'Sunday';
            break;
        case 1:
            dayName = 'Monday';
            break;
        case 2:
            dayName = 'Tuesday';
            break;
        case 3:
            dayName = 'Wednesday';
            break;
        case 4:
            dayName = 'Thursday';
            break;
        case 5:
            dayName = 'Friday';
            break;
        case 6:
            dayName = 'Saturday';
            break;
        default:
            dayName = 'Invalid day';
    }
    return dayName
}


const generateMatrix = (quintets, days, hours, timeSlotToString) => {
    let matrix = [...new Array(hours)].map(() => new Array(days));

    for (let h = 0; h < hours; h++) {
        for (let d = 0; d < days; d++) {
            matrix[h][d] = [];
        }
    }

    for (let i = 0; i < quintets.length; i++) {
        matrix[quintets[i].hour][quintets[i].day].push(quintets[i]);
    }

    let displayMatrix = [...new Array(hours)].map(() => new Array(days));
    for (let h = 0; h < hours; h++) {
        for (let d = 0; d < days; d++) {
            displayMatrix[h][d] = timeSlotToString(matrix[h][d]);
        }
    }

    return displayMatrix;
}

const TabularTimeTable = ({days, hours, quintets, timeSlotToString}) => {
    const classes = useStyles();
    const [rows, setRows] = useState([]);

    useEffect(() => {
        setRows(generateMatrix(quintets, days, hours, timeSlotToString))
    }, [quintets])

    return <TableContainer component={Paper}>
        <Table sx={{minWidth: 650}} aria-label="simple table" className={classes.table}>
            <TableHead>
                <TableRow>
                    <TableCell>Hour</TableCell>
                    {Array.from(Array(days).keys()).map(day => <TableCell sx={{padding: "5px"}}><Typography
                        className={classes.typography}>{convertNumToDay(day)}</Typography></TableCell>)}
                </TableRow>
            </TableHead>
            <TableBody>
                {rows.map((row, index) => {
                    return (
                        <TableRow
                            sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                            <TableCell component="th" scope="row">
                                {index}
                            </TableCell>
                            {row.map((day) => <TableCell sx={{padding: "5px"}}>{day.split("\n").map(lesson =>
                                <Typography
                                    className={classes.typography}>{lesson}</Typography>)}</TableCell>)}
                        </TableRow>
                    )
                })}
            </TableBody>
        </Table>
    </TableContainer>;
}

export default TabularTimeTable;