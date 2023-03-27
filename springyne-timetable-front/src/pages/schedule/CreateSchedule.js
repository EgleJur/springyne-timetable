import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { FormControl, MenuItem, Select, InputLabel } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from "dayjs";
import { apiUrl } from "../../App";

function CreateSchedulePage() {
  const [name, setName] = useState("");
  const today = dayjs().format("YYYY-MM-DD");
  const [startDateValue, setStartDateValue] = useState(null);
  const [endDateValue, setEndDateValue] = useState(null);
  const [selectedGroup, setSelectedGroup] = useState("");
  const [groups, setGroups] = useState([]);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [nameError, setNameError] = useState(false);
  const [startDateError, setStartDateError] = useState(false);
  const [endDateError, setEndDateError] = useState(false);
  const [groupError, setGroupError] = useState(false);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/groups/`)
      .then((response) => response.json())
      .then(setGroups);
  }, []);

  const prefillName = (groupId) => {
    let group = groups?.filter((group) => group.id === groupId);
    setName(
      group[0].name + " " + group[0].program.name + " " + group[0].shift.name
    );
    setNameError(false);
  };

  const createNewSchedule = (e) => {
    e.preventDefault();
    setNameError(false);
    setGroupError(false);
    setStartDateError(false);
    setEndDateError(false);
    if (
      name === "" ||
      selectedGroup === "" ||
      startDateValue === null ||
      endDateValue === null ||
      startDateValue > endDateValue ||
      startDateValue < today
    ) {
      if (name === "") {
        setNameError(true);
      }
      if (selectedGroup === "") {
        setGroupError(true);
      }
      if (
        startDateValue === null ||
        startDateValue < today ||
        (startDateValue > endDateValue && endDateValue !== null)
      ) {
        setStartDateError(true);
      }
      if (endDateValue === null || endDateValue < today || endDateValue < startDateValue) {
        setEndDateError(true);
      }
    } else {
      const startDate = dayjs(startDateValue).format("YYYY-MM-DD");
      const endDate = dayjs(endDateValue).format("YYYY-MM-DD");
      fetch(`${apiUrl}/api/v1/schedules?groupId=${selectedGroup}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          startDate,
          endDate,
        }),
      }).then((result) => {
        if (result.ok) {
          setName("");
          setStartDateValue(null);
          setEndDateValue(null);
          setSelectedGroup("");
          setNameError(false);
          setGroupError(false);
          setStartDateError(false);
          setEndDateError(false);
          setSuccess(true);
          setFailure(false);
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
        }
      });
    }
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują tvarkaraštį</h2>

      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
        </Alert>
      </Collapse>

      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>

      <form noValidate>
        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-group-label" error={!!groupError} required>
            Pasirinkite grupę
          </InputLabel>
          <Select
            error={!!groupError}
            labelId="select-group-label"
            id="select-group"
            label="Pasirinkite grupę"
            value={selectedGroup}
            onChange={(e) => {
              setGroupError(false);
              setSelectedGroup(e.target.value);
              prefillName(e.target.value);
            }}
            required
          >
            {groups?.map((group) => (
              <MenuItem
                value={group.id}
                key={group.id}
                disabled={group.deleted}
              >
                {group.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <DatePicker
          className="mb-3"
          label="Pradžios data"
          // inputFormat="mmmm-MM-dd"
          value={startDateValue}
          disablePast
          maxDate={endDateValue}
          onChange={(newValue) => {
            console.log(newValue);
            setStartDateValue(newValue);
          }}
          renderInput={(params) => (
            <TextField
              fullWidth
              size="small"
              required
              {...params}
              error={!!startDateError}
            />
          )}
        />
        <DatePicker
          className="mb-3"
          label="Pabaigos data"
          // inputFormat="yyyy-MM-dd"
          value={endDateValue}
          disablePast
          minDate={startDateValue}
          onChange={(newValue) => {
            console.log(newValue);
            setEndDateValue(newValue);
          }}
          onError={() => setEndDateError(true)}
          renderInput={(params) => (
            <TextField
              fullWidth
              size="small"
              required
              {...params}
              error={!!endDateError}
            />
          )}
        />

        <TextField
          error={!!nameError}
          onChange={(e) => {
            setName(e.target.value);
          }}
          value={name}
          id="schedule-name"
          label="Pavadinimas"
          helperText="Pavadinimas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />

        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewSchedule}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateSchedulePage;
