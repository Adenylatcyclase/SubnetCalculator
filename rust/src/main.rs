use std::str::FromStr;
use std::fmt;

struct IP {
    ip: u32,
    mask: u32,
}

impl IP {
    pub fn from_str(ip: &str, mask: &str) -> Self {
        IP{ip: IP::parse_string(ip), mask: IP::parse_string(mask)}
    }

    fn parse_string(s: &str) -> u32 {
        let ss: Vec<&str> = s.split(".").collect();
        if ss.len() > 4 {
            panic!("Only ipv4 supported")
        }
        let mut num = 0;
        for i in 0..4 {
            let x = u32::from_str(ss[i]).expect("NaN");
            if x > 255 {
                panic!("Only values  0 to 255 supported");
            }
            num = num << 8;
            num += x;
        }
        return num;
    }

    fn parse_number(num: &u32) -> String{
        let mut s = String::new();
        for i in 0..4 {
            s.push_str(&(((num >> (3 - i) * 8) & 255 as u32) as u32).to_string());
            if i < 3 {
                s.push_str(".");
            }
        }
        return s;
    }

    pub fn get_network(&self) -> String {
        IP::parse_number(&(self.ip & self.mask))
    }

    pub fn get_host(&self) -> String {
        IP::parse_number(&(self.ip & (!self.mask)))
    }

    pub fn get_first_host(&self) -> String {
        IP::parse_number(&((self.ip & self.mask) | 1))
    }
    pub fn get_last_host(&self) -> String {
        IP::parse_number(&((self.ip & self.mask) | (!self.mask & u32::max_value())))
    }
    pub fn get_broadcast(&self) -> String {
        IP::parse_number(&((self.ip & self.mask) | (!self.mask & (u32::max_value() - 1))))
    }

    pub fn get_host_count(&self) -> String {
        (!self.mask & u32::max_value() - 1).to_string()
    }
    pub fn get_info(&self) -> String {
        let mut s = String::new();
        s.push_str(&format!("IP: {}\nSubnetmask: {}\n", IP::parse_number(&self.ip), IP::parse_number(&self.mask)));
        s.push_str(&format!("Network: {} /XX\n", self.get_network()));
        s.push_str(&format!("Broadcast: {}\n", self.get_broadcast()));
        s.push_str(&format!("Host: {}\n", self.get_host()));
        s.push_str(&format!("First Host: {}\n", self.get_first_host()));
        s.push_str(&format!("Last Host: {}\n", self.get_last_host()));
        s.push_str(&format!("Hosts: {}\n", self.get_host_count()));
        s
    }
}

impl fmt::Display for IP {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "({}, {})", IP::parse_number(&self.ip), IP::parse_number(&self.mask))
    }
}
fn main() {
    let args: Vec<String>= std::env::args().collect();
    if args.len() <= 2 {
        panic!("Needs IP address and Subnet address!")
    }
    let ip = &args[1];
    let mask = &args[2];
    let address = IP::from_str(ip, mask);
    println!("{}", address.get_info());
}
