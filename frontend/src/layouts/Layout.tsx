import { ROLE, useUserStore } from "@/stores/user-store";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import Header from "./Header";
import Footer from "./Footer";
import { getToken } from "@/utils/token";
import fetcher from "@/utils/fetcher";
import HomeFooter from "./HomeFooter";
import HomeHeader from "./HomeHeader";

const NO_HEADER_ROUTES = [
  "/login",
  "/register",
  "/admin-login",
  "/super-admin-login",
];
const PUBLIC_ROUTES = new Set([
  "/login",
  "/register",
  "/admin-login",
  "/super-admin-login",
  "/dashboard",
]);
const DEFAULT_PUBLIC_ROUTE = "/dashboard";

const isProtectedRoute = (route: string) => !PUBLIC_ROUTES.has(route);

function Layout({ children }: any) {
  const router = useRouter();
  const currentRoute = router.pathname;
  const user = useUserStore.getState().user;
  const setUser = useUserStore.use.setUser();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = getToken();
    if (currentRoute === "/") {
      if (token) {
        if (user.registerFor === "Sports Person Recruitment") {
          router.push("/sportpersonrecruitment");
        } else if (user.registerFor === "Young Professional Law") {
          router.push("/youngprofessionalrecruitment");
        } else if (user?.roles?.[0]?.roleId === ROLE.ADMIN) {
          router.push("/dashboard");
        } else {
          router.push("/super-admin-dashboard");
        }
      } else {
        router.push("/login");
      }
    }
    if (!token) {
      setStatus(false);
      return;
    }
    const controller = new AbortController();
    const { signal } = controller;
    fetcher("/verify", "POST", null, signal).then((json) => {
      if (signal.aborted) {
        return;
      }
      if (!json || json.error) {
        setStatus(false);
        return;
      }
      setStatus(json.verified);
      if (json.verified) {
        const { user } = json;
        setUser(user);
      }
    });
    return () => controller.abort();
  }, [currentRoute]);

  const setStatus = (isValid: boolean) => {
    setTimeout(() => {
      if (!isValid && isProtectedRoute(currentRoute)) {
        router.replace(DEFAULT_PUBLIC_ROUTE);
      }
      if (isValid && !isProtectedRoute(currentRoute)) {
        if (user.registerFor === "Sports Person Recruitment") {
          router.push("/sportpersonrecruitment");
        } else if (user.registerFor === "Young Professional Law") {
          router.push("/youngprofessionalrecruitment");
        } else if (user?.roles?.[0]?.roleId === ROLE.ADMIN) {
          router.push("/dashboard");
        } else {
          router.push("/super-admin-dashboard");
        }
      }
      setIsAuthenticated(isValid);
      setIsLoading(false);
    }, 500);
  };

  return (
    !isLoading &&
    (isAuthenticated ||
      (!isAuthenticated && PUBLIC_ROUTES.has(currentRoute))) && (
      <div className="flex flex-column w-100 h-100 ">
        <div
          style={{
            // marginTop: isProtectedRoute(currentRoute) ? "4rem" : "0rem",
            // marginBottom: isProtectedRoute(currentRoute) ? "2rem" : "0rem",
            display: "flex",
            flexDirection: "column",
            // height: isProtectedRoute(currentRoute) ? "" : "100vh",
            height: "100vh",
          }}
        >
          {!NO_HEADER_ROUTES.includes(currentRoute) ? (
            <Header />
          ) : (
            <HomeHeader />
          )}
          {children}
          {NO_HEADER_ROUTES.includes(currentRoute) ? (
            <Footer />
          ) : (
            <HomeFooter />
          )}
        </div>
      </div>
    )
  );
}

export default Layout;
