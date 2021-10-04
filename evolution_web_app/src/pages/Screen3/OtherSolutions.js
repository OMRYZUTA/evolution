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

const OtherSolutions = ({othersolutionsList}) => {
    const classes = useStyles();

    // if it's undefined
    if (!othersolutionsList) {
        return (<CircularIndeterminate/>);
    }

    return (
        <Paper>
            <Grid container direction={"column"} className={classes.root}>
                <Typography>
                    other users are solving the same problem:
                </Typography>
                {othersolutionsList.map(solutionInfo => {
                    return (
                        <Grid item>
                            <Grid container className={classes.settings}>
                                <DataGrid
                                    rows={rows}
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