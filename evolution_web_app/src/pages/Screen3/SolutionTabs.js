import AlgorithmConfiguration from "./AlgorithmConfiguration";
import Box from '@mui/material/Box';
import DescriptionIcon from '@mui/icons-material/Description';
import EqualizerIcon from '@mui/icons-material/Equalizer';
import {makeStyles} from '@mui/styles';
import PropTypes from 'prop-types';
import React from 'react';
import Tab from '@mui/material/Tab';
import Tabs from '@mui/material/Tabs';
import TimetableDetails from "./TimetableDetails";
import TeacherView from "./TeacherView";
import SchoolClassView from "./SchoolClassView";
import RawView from "./RawView";

TabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.any.isRequired,
    value: PropTypes.any.isRequired,
};

function TabPanel(props) {
    const {children, value, index, ...other} = props;
    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`scrollable-force-tabpanel-${index}`}
            aria-labelledby={`scrollable-force-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box p={3}>
                    {/*<Typography>*/}
                    {children}
                    {/*</Typography>*/}
                </Box>
            )}
        </div>
    );
}

const a11yProps = (index) => {
    return {
        id: `scrollable-force-tab-${index}`,
        'aria-controls': `scrollable-force-tabpanel-${index}`,
    };
}

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        width: '100%',
        backgroundColor: "#FFFFC5", // yellow
    },
}));

const SolutionTabs = ({days, hours, quintets, teachersObject, schoolClassesObject}) => {
    const classes = useStyles();
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <div className={classes.root}>
            <Tabs
                value={value}
                onChange={handleChange}
                variant="scrollable"
                scrollButtons={true}
                indicatorColor="primary"
                textColor="primary"
                aria-label="scrollable force tabs example">
                <Tab label="Teacher view" icon={<EqualizerIcon/>} {...a11yProps(0)} />
                <Tab label="School Class view" icon={<DescriptionIcon/>} {...a11yProps(1)} />
                <Tab label="Raw view" icon={<DescriptionIcon/>} {...a11yProps(2)} />
            </Tabs>

            <TabPanel value={value} index={0}>
                <TeacherView quintets={quintets} hours={hours} days={days} teachersObject={teachersObject}/>
            </TabPanel>

            <TabPanel value={value} index={1}>
                <SchoolClassView quintets={quintets} hours={hours} days={days} schoolClassesObject={schoolClassesObject}/>
            </TabPanel>
            <TabPanel value={value} index={2}>
                <RawView quintets={quintets} />
            </TabPanel>

        </div>
    );
}

export default SolutionTabs;
