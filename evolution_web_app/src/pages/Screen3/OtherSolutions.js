import CircularIndeterminate from "../../components/CircularIndeterminate";
import Grid from "@mui/material/Grid";
import {makeStyles} from "@mui/styles";
import Paper from "@mui/material/Paper";
import React from 'react';
import Typography from "@mui/material/Typography";
import DataGrid from "../../components/DataGrid";

const useStyles = makeStyles((theme) => ({
    root: {
        spacing: 2,
        justifyContent: "space-between"
    },
    settings: {
        spacing: 2,
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

const OtherSolutions = ({otherSolutionsList}) => {
    const classes = useStyles();

    // String userName;
    // Double bestScore;
    // Integer currentGeneration;
    const columns = [
        {field: 'id', headerName: 'ID', width: 90},
        {
            field: 'firstName',
            headerName: 'First name',
            width: 150,
            editable: true,
        },
        {
            field: 'lastName',
            headerName: 'Last name',
            width: 150,
            editable: true,
        },
        {
            field: 'age',
            headerName: 'Age',
            type: 'number',
            width: 110,
            editable: true,
        },
        {
            field: 'fullName',
            headerName: 'Full name',
            description: 'This column has a value getter and is not sortable.',
            sortable: false,
            width: 160,
            valueGetter: (params) =>
                `${params.getValue(params.id, 'firstName') || ''} ${
                    params.getValue(params.id, 'lastName') || ''
                }`,
        },
    ];

    // if it's undefined
    if (!otherSolutionsList) {
        return (<CircularIndeterminate/>);
    }

    return (
        <Paper>
            <Grid container direction={"column"} className={classes.root}>
                <Typography>
                    other users are solving the same problem:
                </Typography>
                {otherSolutionsList.map(solutionInfo => {
                    return (
                        <Grid item>
                            <Grid container className={classes.settings}>
                                <DataGrid
                                    rows={otherSolutionsList}
                                    columns={columns}
                                    pageSize={5}
                                    rowsPerPageOptions={[5]}
                                    checkboxSelection
                                    disableSelectionOnClick
                                />
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    {solutionInfo.userName}
                                </Typography>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    {solutionInfo.bestScore}
                                </Typography>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    {solutionInfo.currentGeneration}
                                </Typography>
                            </Grid>
                        </Grid>
                    )
                })}
            </Grid>
        </Paper>
    );
}

export default OtherSolutions;