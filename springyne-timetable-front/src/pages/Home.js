import { useEffect, useState } from "react";
import { apiUrl } from "../App";

function HomePage() {

    const [version, setVersion] = useState([]);

    useEffect(() => {
        fetch(`${apiUrl}/api/v1/timetable/info`)
        .then((response) => response.text())
        .then(setVersion);
    },[])

    return ( <div className="my-page">
    {/* page content */}
  <div className="mx-3">
        <h1 className="my-5">Hello</h1>
        <h4>Application version: {version}</h4>
    </div> </div>);
}

export default HomePage;