import AlgorithmConfiguration from "./AlgorithmConfiguration";
import Box from '@material-ui/core/Box';
import DescriptionIcon from '@material-ui/icons/Description';
import EqualizerIcon from '@material-ui/icons/Equalizer';
import {makeStyles} from '@material-ui/core/styles';
import PropTypes from 'prop-types';
import React from 'react';
import Tab from '@material-ui/core/Tab';
import Tabs from '@material-ui/core/Tabs';
import TimetableDetails from "./TimetableDetails";
import Typography from '@material-ui/core/Typography';

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
                    <Typography>{children}</Typography>
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

const InfoTabs = ({stats, algorithmConfiguration, handleAlgorithmConfigChange, timetable}) => {
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
                scrollButtons="on"
                indicatorColor="primary"
                textColor="primary"
                aria-label="scrollable force tabs example"
            >
                <Tab label="Algorithm Configuration" icon={<EqualizerIcon/>} {...a11yProps(0)} />
                <Tab label="Timetable Details" icon={<DescriptionIcon/>} {...a11yProps(1)} />
            </Tabs>

            <TabPanel value={value} index={0}>
                <AlgorithmConfiguration algorithmConfiguration={algorithmConfiguration}
                                        handleSave={handleAlgorithmConfigChange}
                                        andleCancel={() => console.log("cancel algoConfig change")}/>
            </TabPanel>

            <TabPanel value={value} index={1}>
                <TimetableDetails timetable={timetable}/>
            </TabPanel>

        </div>
    );
}

export default InfoTabs;
