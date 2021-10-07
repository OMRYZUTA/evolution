import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {useState} from "react";

function createData(hour, sunday, monday, tuesday, Wednesday) {
    return {hour, sunday, monday, tuesday, Wednesday};
}


const timeSlotToString = (quintets) => {
    let slotString = "";

    for (let i = 0; i < quintets.length; i++) {
        let id1 = quintets[i].schoolClassID;
        let s1 = quintets[i].schoolClassName;
        let id2 = quintets[i].subjectID;
        let s2 = quintets[i].subjectName;
        slotString += `${id1} ${s1}, ${id2} ${s2}\n`;
    }

    return slotString;
}


const generateMatrix = (quintets, days, hours) => {
    let matrix = [...new Array(hours)].map(elem => new Array(days));

    for (let h = 0; h < hours; h++) {
        for (let d = 0; d < days; d++) {
            matrix[h][d] = [];
        }
    }

    for (let i = 0; i < quintets.length; i++) {
        matrix[quintets[i].hour][quintets[i].day].push(quintets[i]);
    }

    let displayMatrix = [...new Array(hours)].map(elem => new Array(days));
    for (let h = 0; h < hours; h++) {
        for (let d = 0; d < days; d++) {
            displayMatrix[h][d] = timeSlotToString(matrix[h][d]);
        }
    }

    return displayMatrix;
}

const TeacherView = ({quintets, days, hours}) => {
    const rows = generateMatrix(quintets, days, hours);

    return (
        <TableContainer component={Paper}>
            <Table sx={{minWidth: 650}} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Hour</TableCell>
                        {Array.from(Array(days).keys()).map(day => <TableCell>{day}</TableCell>)}
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
                                {row.map((day) => <TableCell>{day}</TableCell>)}
                            </TableRow>
                        )
                    })}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default TeacherView;