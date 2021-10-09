import React from 'react';
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";

function getSortField(quintet) {
    return `${quintet.day}-${quintet.hour}-${quintet.schoolClassID}-${quintet.teacherID}`;
}

const RawView = ({quintets}) => {
    // Solution Raw view - sorted Day->Hour->class->teacher
    const sortedQuintets = quintets.sort((q1, q2) => {
        return q1.day - q2.day || q1.hour - q2.hour || q1.schoolClassID - q2.schoolClassID || q1.teacherID - q2.teacherID;
    });

    return (
        <Grid container direction={"column"}>
            <Typography>
                {`<D,H,C,T,S>`}
            </Typography>
            {sortedQuintets.map(quintet => {
                return (
                    <Grid item>
                        <Typography>
                            {`<${quintet.day},${quintet.hour},${quintet.schoolClassID},${quintet.teacherID}>`}
                        </Typography>
                    </Grid>
                )
            })}
        </Grid>
    )
}
export default RawView;