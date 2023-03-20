import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Select, MenuItem } from "@mui/material";
import { apiUrl } from "../App";

function EditModuleSubjects(props) {
  const params = useParams();
  const [subjects, setSubjects] = useState([]);
  const [availabeSubjects, setAvailableSubjects] = useState([]);
  const [chosenSubject, setChosenSubject] = useState("");

  const fetchSubjects = () => {
    fetch(`${apiUrl}/api/v1/modules/subjects/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  };

  const fetchAvailableSubjects = () => {
    fetch(`${apiUrl}/api/v1/modules/subjects/available/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setAvailableSubjects(jsonResponse));
  };

  useEffect(() => fetchSubjects, []);

  useEffect(() => fetchAvailableSubjects, []);

  const handleAddModule = () => {
    fetch(`${apiUrl}/api/v1/subjects/${chosenSubject}/addModule/` + params.id, {
      method: "PATCH",
    })
      .then(fetchSubjects)
      .then(fetchAvailableSubjects);
  };

  return (
    <div className="mb-2">
      {subjects.length > 0 ? (
        <div className="row mb-2">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">Dalykai</div>
          <div className="col-md-8 mb-2 mb-md-0">
            {subjects?.map((subject) => (
              <div className={subject.deleted ? "text-black-50" : ""}>
                <div className="row mb-2" key={subject.id}>
                  <div className="col-md">{subject.name}</div>
                  {/* <div className="col-md">{subject.description}</div>
                  <div className="col-md">{subject.last_Updated}</div> */}
                  {subject.deleted ? (
                    <div className="col-md">Dalykas ištrintas</div>
                  ) : (
                    <div className="col-md"></div>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      ) : (
        ""
      )}

      <div className="row">
        <div className="col-md-4 mb-2 mb-md-0 fw-bold">
          <label htmlFor="select-available-subject">Pridėti dalyką</label>
        </div>
        <div className="col-md-8">
          <form>
            <div className="row">
              <div className="col-md-9 mb-2 mb-md-0">
                <Select
                  disabled={props.disabled}
                  id="select-available-subject"
                  size="small"
                  fullWidth
                  value={chosenSubject}
                  onChange={(e) => setChosenSubject(e.target.value)}
                >
                  {availabeSubjects?.map((subject) => (
                    <MenuItem value={subject.id} key={subject.id}>
                      {subject.name}
                    </MenuItem>
                  ))}
                </Select>
              </div>
              <div className="col-md-2">
                <button
                  className="btn btn-primary"
                  onClick={handleAddModule}
                  disabled={props.disabled}
                >
                  Pridėti
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default EditModuleSubjects;
