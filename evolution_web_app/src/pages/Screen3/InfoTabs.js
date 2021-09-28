import React from 'react';
import PropTypes from 'prop-types';
import {makeStyles} from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import EqualizerIcon from '@material-ui/icons/Equalizer';
import DescriptionIcon from '@material-ui/icons/Description';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import AlgorithmConfiguration from "./AlgorithmConfiguration";
import Button from "@material-ui/core/Button";
import {ButtonGroup} from "@material-ui/core";

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

TabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.any.isRequired,
    value: PropTypes.any.isRequired,
};

function a11yProps(index) {
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

export default function IconTabs({stats, engineSettings, handleEngineSettingsChanged}) {
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
                <Tab label="Timetable details" icon={<DescriptionIcon/>} {...a11yProps(0)} />
                <Tab label="Engine settings" icon={<EqualizerIcon/>} {...a11yProps(1)} />
            </Tabs>
            <TabPanel value={value} index={0}>
                Timetable Details
            </TabPanel>
            <TabPanel value={value} index={1}>
                <AlgorithmConfiguration algorithmConfiguration={engineSettings}
                                        handleEngineSettingsChanged={handleEngineSettingsChanged}/>
                <ButtonGroup>
                    <Button onClick={() => {
                        console.log("onClick save")
                    }}>Save</Button>
                    <Button onClick={() => {
                        console.log("onClick cancel")
                    }}>Cancel</Button>
                </ButtonGroup>
            </TabPanel>
        </div>
    );
}
