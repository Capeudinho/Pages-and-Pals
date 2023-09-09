import React, { useState, useEffect, useContext } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import axios from "axios"; 
import api from "../../../services/api.js";

import BookCard from "../../common/book/card.js";
import AdvancedSearch from "./advanced.js";

import overlayContext from "../../context/overlay.js";
import scrollContext from "../../context/scroll.js";
import searchContext from "../../context/search.js";

function ResultsList() {
    const { overlay, setOverlay } = useContext(overlayContext);
    const { scroll, setScroll } = useContext(scrollContext);
    const { searchParams } = useContext(searchContext); 
    console.log('searchParams:', searchParams);
    
    const [searchResults, setSearchResults] = useState([]);

    useEffect
        (
            () => {
                let mounted = true;
                const runEffect = async () => {
                    if
                        (
                        !overlay &&
                       // searchResults?.length < searchParams.maxResults &&
                        scroll?.target?.scrollHeight - scroll?.target?.scrollTop <= scroll?.target?.offsetHeight + 100
                    ) {
                        await loadResults(false);
                    }
                }
                runEffect();
                return (() => { mounted = false });
            },
            [scroll, searchResults, searchParams, overlay]
        );

    async function loadResults(overwrite) {
        if (overwrite /*|| searchResults?.length < searchParams.maxResults*/) {
            var offset;
            if (overwrite) {
                offset = 0;
            }
            else {
                offset = searchResults?.length;
            }
            try {
                //var response;
                setOverlay(true);
                const response = await axios.get(
                    "/advancedsearch",
                    {
                        params:
                        {
                            offset: offset,
                            limit: 20,
                            params: searchParams
                        }
                    }
                );
                setOverlay(false);
                if (overwrite) {
                    setSearchResults(response.data);
                }
                else {
                    setSearchResults([...searchResults, ...response?.data]);
                }
            }
            catch (exception) {
                setOverlay(false);
                if (exception?.response?.data === "authentication failed") {
                    localStorage.clear();
                    //setLoggedAccount(null);
                }
                //navigate("/");
            }
        }
    }
    console.log('searchResults:', searchResults);

}

export default ResultsList;