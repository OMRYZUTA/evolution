import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

function createData(hour, sunday, monday, tuesday, Wednesday) {
    return {hour, sunday, monday, tuesday, Wednesday};
}

const rows = [
    createData('08:00', "<C,S>", "<C,S>", "<C,S>", "<C,S>"),
    createData('10:00', "<C,S>", "<C,S>", "<C,S>", "<C,S>"),
    createData('09:00', "<C,S>", "<C,S>", "<C,S>", "<C,S>"),
    createData('11:00', "<C,S>", "<C,S>", "<C,S>", "<C,S>"),
    createData('12:00', "<C,S>", "<C,S>", "<C,S>", "<C,S>"),
];

const timeSlotToString = (quintets) => {
    let slotString = "";

    for (let quintet in quintets) {
//            <teacher id, teacher name, subject id, subject name>
        let id1 = quintet.schoolClass.id
        let s1 = quintet.schoolClass.name;
        let id2 = quintet.subject.id;
        let s2 = quintet.subject.name;
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

    for (let q in quintets) {
        matrix[q.hour][q.day].add(q);
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
    console.log("*******************************          teacher view **************************************")
    console.log(generateMatrix(quintets, days, hours));

    return (
        <TableContainer component={Paper}>
            <Table sx={{minWidth: 650}} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Dessert (100g serving)</TableCell>
                        <TableCell align="right">Calories</TableCell>
                        <TableCell align="right">Fat&nbsp;(g)</TableCell>
                        <TableCell align="right">Carbs&nbsp;(g)</TableCell>
                        <TableCell align="right">Protein&nbsp;(g)</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => (
                        <TableRow
                            key={row.name}
                            sx={{'&:last-child td, &:last-child th': {border: 0}}}
                        >
                            <TableCell component="th" scope="row">
                                {row.name}
                            </TableCell>
                            <TableCell align="right">{row.calories}</TableCell>
                            <TableCell align="right">{row.fat}</TableCell>
                            <TableCell align="right">{row.carbs}</TableCell>
                            <TableCell align="right">{row.protein}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default TeacherView;