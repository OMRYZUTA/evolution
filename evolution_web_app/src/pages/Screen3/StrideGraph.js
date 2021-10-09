import * as React from 'react';
import {useEffect, useState} from 'react';
import Paper from '@mui/material/Paper';
import {makeStyles} from '@mui/styles'
import {ArgumentAxis, Chart, LineSeries, ValueAxis,} from '@devexpress/dx-react-chart-material-ui';

const useStyles = makeStyles(() => ({
    paper: {
        width: "80%",
    }
}));



const StrideGraph = ({strideData}) => {
    const classes = useStyles();
    const [data, setData] = useState([]);

    useEffect(() => {
        setData(strideData.map(stride => ({"argument": stride.generationNum, "value": stride.bestScoreInGeneration})
        ))
    }, [strideData]);

    return (
        <Paper className={classes.paper}>
            <Chart
                data={data}
            >
                <ArgumentAxis/>
                <ValueAxis/>
                <LineSeries valueField="value" argumentField="argument"/>
            </Chart>
        </Paper>
    )
}

export default StrideGraph;