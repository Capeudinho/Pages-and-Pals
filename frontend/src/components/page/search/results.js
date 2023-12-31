import React, { useState, useEffect, useContext, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../../../services/api.js";

import BookCard from "../../common/book/card.js";
import BookshelfCard from "../../common/bookshelf/card.js";

import overlayContext from "../../context/overlay.js";
import scrollContext from "../../context/scroll.js";
import loggedAccountContext from "../../context/loggedAccount.js";

import "./results.css";

function SearchResults() {
    const { overlay, setOverlay } = useContext(overlayContext);
    const { scroll, setScroll } = useContext(scrollContext);
    const [results, setResults] = useState([]);
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const navigate = useNavigate();
    const { search } = useLocation();
    const moreResults = useRef(false);

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                await loadResults(true);
            }
            runEffect();
            return (()=>{mounted = false});
        },
        [search]
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if
                (
                    !overlay &&
                    moreResults.current &&
                    scroll?.target?.scrollHeight - scroll?.target?.scrollTop <= scroll?.target?.offsetHeight + 100
                )
                {
                    await loadResults(false);
                }
            }
            runEffect();
            return (()=>{mounted = false});
        },
        [scroll, results]
    );

    async function loadResults(overwrite)
    {
        try
        {
            var newSearch;
            var response;
            if (overwrite)
            {
                newSearch = search + "&limit=20&offset=0";
            }
            else
            {
                newSearch = search + "&limit=20&offset=" + results.length;
            }
            setOverlay(true);
            if (loggedAccount?.id !== undefined)
            {
                response = await api.get
                (
                    "/book/findbyadvancedauthenticated"+newSearch,
                    {
                        headers:
                        {
                            email: loggedAccount?.email,
                            password: loggedAccount?.password
                        }
                    }
                );
            }
            else
            {
                response = await api.get("/book/findbyadvanced"+newSearch);
            }
            if (response.data.length >= 20) {
                moreResults.current = true;
            }
            if (overwrite)
            {
                setResults(response?.data);
            }
            else
            {
                setResults([...results, ...response?.data]);
            }
            setOverlay(false);
        }
        catch (exception) {
            setOverlay(false);
            if (exception?.response?.data === "authentication failed") {
                localStorage.clear();
                setLoggedAccount(null);
            }
            navigate("/");
        }
    }
    return (
        <div className="searchResultsArea">
            {
                results?.map
                (
                    (searchResult, searchResultIndex) => {
                        return (
                            searchResult?.title !== undefined ?
                            <BookCard
                            key={searchResultIndex}
                            book={searchResult}
                            bookIndex={null}
                            remove={null}
                            removeable={false}
                            manageable={true}
                            /> : 
                            <BookshelfCard
                            bookshelf={searchResult}
                            linkable={true}
                            key={searchResultIndex}
                            />
                        );
                    }
                )
            }

        </div>
    );
}
export default SearchResults;